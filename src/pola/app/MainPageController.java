/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pola.app;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import pola.FXMain;

/**
 * FXML Controller class
 *
 * @author Siroj Nur Ulum
 */
public class MainPageController implements Initializable {
//var

    @FXML
    TabPane tab;
    @FXML
    Tab tabHistogram;
    @FXML
    Tab tabEkualisasiHistogram;
    @FXML
    Tab tabChainCode;
    @FXML
    Tab tabKodeBelok;
    @FXML
    Tab tabTulang;
    @FXML
    Tab tabHuruf;
    @FXML
    Tab tabMainPage;
    @FXML
    ImageView ivMainOri;
    @FXML
    ImageView ivMainGray;
    @FXML
    ImageView ivMainGrayEq;
    @FXML
    ImageView ivMainBw;
    @FXML
    Button btnPilihGambar;
    @FXML
    ImageView ivHistogram;
    @FXML
    LineChart chartHistogram;
    @FXML
    ImageView ivEqHistogram;
    @FXML
    LineChart chartEqHistogram;
    @FXML
    Slider sliderEqHistogramFrom;
    @FXML
    Slider sliderEqHistogramTo;
    @FXML
    ImageView ivChainCode;
    @FXML
    ImageView ivChainCodeBolong;
    @FXML
    TextArea textChainCode;
    @FXML
    ImageView ivKodeBelok;
    @FXML
    ImageView ivKodeBelokBolong;
    @FXML
    TextArea textKodeBelok;
    @FXML
    TextArea textTulangChain;
    @FXML
    ImageView ivTulangBw;
    @FXML
    ImageView ivTulangResult;
    @FXML
    ImageView ivHuruf;
    @FXML
    Label textHuruf;
    //
    File fileImageOri;
    BufferedImage buffOri, buffGray, buffGrayEq, buffBw, buffTulang, buffBolong;
    Gambar gambar;
    List<List<String>> chainData, belokData;
    HashMap<String, String> td = new HashMap<>();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (ImageView iv : new ImageView[] {
                ivMainOri, ivMainGray, ivMainGrayEq, ivMainBw,
                ivHistogram, ivEqHistogram,
                ivChainCode, ivChainCodeBolong,
                ivKodeBelok, ivKodeBelokBolong,
                ivTulangBw, ivTulangResult,
                ivHuruf }) {
            iv.setUserData(new double[] { iv.getFitWidth(), iv.getFitHeight() });
        }
        
        setTabAccess(false);
        setMap();
        
        tab.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
            if (newValue == tabHistogram) {
                openTabHistogram();
            } else if (newValue == tabEkualisasiHistogram) {
                openTabEkualisasiHistogram(0, 255);
            } else if (newValue == tabChainCode) {
                openTabChainCode();
            } else if (newValue == tabKodeBelok) {
                openTabKodeBelok();
            } else if (newValue == tabTulang) {
                openTabTulang();
            } else if (newValue == tabHuruf) {
                openTabHuruf();
            }
        });
    }
    private FXMain main;
    
    public void setMain(FXMain main) {
        this.main = main;
    }
    
    private void setTabAccess(boolean active) {
        tabChainCode.setDisable(!active);
        tabEkualisasiHistogram.setDisable(!active);
        tabHistogram.setDisable(!active);
        tabKodeBelok.setDisable(!active);
        tabTulang.setDisable(!active);
        tabHuruf.setDisable(!active);
    }
    
    @FXML
    public void chooseImage() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG"), new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG"));
        fileImageOri = new FileChooser().showOpenDialog(main.getPrimaryStage());
        buffOri = ImageIO.read(fileImageOri);
        
        if (fileImageOri != null) {
            
            // reset
            chartHistogram.getData().clear();
            chartEqHistogram.getData().clear();
            chainData = null;
            belokData = null;
            textTulangChain.setText("");
            
            gambar = new Gambar(fileImageOri);
            
            setBufferedImage();
            setImageView();
            //setTextView(); // tidak perlu
            setTabAccess(true);
        } else {
            // setTabAccess(false); // tidak perlu karena masih pakai gambar yang lama
            main.showAlert("Error", "File Chooser", "No Picture Selected !!!", Alert.AlertType.INFORMATION);
        }
    }
    
    private void openTabHistogram() {
        if (chartHistogram.getData().size() == 0) {
            XYChart.Series seriesR = new XYChart.Series();
            XYChart.Series seriesG = new XYChart.Series();
            XYChart.Series seriesB = new XYChart.Series();
            XYChart.Series seriesGray = new XYChart.Series();
            
            ObservableList dataR = seriesR.getData();
            ObservableList dataG = seriesG.getData();
            ObservableList dataB = seriesB.getData();
            ObservableList dataGray = seriesGray.getData();
            
            for (int i = 0; i < 256; i++) {
                dataR.add(new XYChart.Data(i, gambar.histogram.r[i]));
                dataG.add(new XYChart.Data(i, gambar.histogram.g[i]));
                dataB.add(new XYChart.Data(i, gambar.histogram.b[i]));
                dataGray.add(new XYChart.Data(i, gambar.histogram.gray[i]));
            }
            chartHistogram.getData().add(seriesR);
            chartHistogram.getData().add(seriesG);
            chartHistogram.getData().add(seriesB);
            chartHistogram.getData().add(seriesGray);
            
            seriesR.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(255, 0, 0, 1.0);");
            seriesG.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(0, 255, 0, 1.0);");
            seriesB.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(0, 0, 255, 1.0);");
            seriesGray.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(128, 128, 128, 1.0);");
        }
    }
    
    private void openTabEkualisasiHistogram(int from, int to) {
        if (chartEqHistogram.getData().size() == 0) {
            gambar.equalize(from, to);
            gambar.updateBufferedImage();
            setIvImage(ivEqHistogram, gambar.biEqualized);
            
            XYChart.Series seriesGray = new XYChart.Series();
            XYChart.Series seriesEq = new XYChart.Series();
            ObservableList dataGray = seriesGray.getData();
            ObservableList dataEq = seriesEq.getData();
            for (int i = 0; i < 256; i++) {
                dataGray.add(new XYChart.Data(i, gambar.histogram.gray[i]));
                dataEq.add(new XYChart.Data(i, gambar.histogram.equalized[i]));
            }
            chartEqHistogram.getData().add(seriesGray);
            chartEqHistogram.getData().add(seriesEq);
            seriesGray.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(255, 128, 128, 1.0);");
            seriesEq.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(0, 0, 0, 1.0);");
        }
    }
    
    private void openTabChainCode() {
        if (chainData == null) {
            chainData = Operation.getOp().getChainCode(gambar.biBolong);
            setTextChainCode();
        }
    }
    
    private void openTabKodeBelok() {
        if (belokData == null) {
            openTabChainCode();
            belokData = Operation.getOp().getKodeBelok(chainData);
            setTextKodeBelok();
        }
    }
    
    private void openTabTulang() {
        if (textTulangChain.getText().length() == 0) {
            gambar.tulangin();
            gambar.updateBufferedImage();
            setIvImage(ivTulangResult, gambar.biTulang);
            setIvImage(ivHuruf, gambar.biTulang);
            setTextTulangChain(Operation.getOp().getChainCodeTulang(gambar.biTulang));
        }
    }
    
    private void openTabHuruf() {
        openTabTulang();
    }
    
    private void setBufferedImage() throws IOException {
        buffOri = ImageIO.read(fileImageOri);
        buffGray = gambar.biGrayscale;
        buffGrayEq = gambar.biEqualized;
        buffBw = gambar.biBinary;
        //buffTulang = gambar.biTulang; // tidak perlu
        buffBolong = gambar.biBolong;
        //chainData = Operation.getOp().getChainCode(buffBolong); // tidak perlu
        //belokData = Operation.getOp().getKodeBelok(chainData); // tidak perlu
    }
    
    private void setImageView() {
        setIvImage(ivMainOri, buffOri);
        setIvImage(ivMainGray, buffGray);
        setIvImage(ivMainGrayEq, buffGrayEq);
        setIvImage(ivMainBw, buffBw);
        setIvImage(ivHistogram, buffOri);
        setIvImage(ivEqHistogram, buffGrayEq);
        setIvImage(ivChainCode, buffBw);
        setIvImage(ivChainCodeBolong, buffBolong);
        setIvImage(ivKodeBelok, buffBw);
        setIvImage(ivKodeBelokBolong, buffBolong);
        setIvImage(ivTulangBw, buffOri);
        
        //setIvTulangResult(buffTulang); // tidak perlu
        //setIvHuruf(buffTulang); // tidak perlu
    }
    
    @Deprecated
    private void setTextView() {
        //setTextChainCode(); // tidak perlu
        //setTextKodeBelok(); // tidak perlu
        //setTextTulangChain(Operation.getOp().getChainCodeTulang(buffTulang)); // tidak perlu
    }
    
    private void setIvImage(ImageView iv, BufferedImage image) {
        // reset
        double[] size = (double[]) iv.getUserData();
        iv.setFitWidth(size[0]);
        iv.setFitHeight(size[1]);
        
        // set
        if (image.getWidth() > iv.getFitWidth() || image.getHeight() > iv.getFitHeight()) {
            iv.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            iv.setImage(SwingFXUtils.toFXImage(image, null));
            iv.setFitHeight(image.getHeight());
            iv.setFitWidth(image.getWidth());
        }
    }
    
    @Deprecated
    private void setIvMainOriImage(BufferedImage image) {
        if (image.getWidth() > ivMainOri.getFitWidth() || image.getHeight() > ivMainOri.getFitHeight()) {
            ivMainOri.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivMainOri.setImage(SwingFXUtils.toFXImage(image, null));
            ivMainOri.setFitHeight(image.getHeight());
            ivMainOri.setFitWidth(image.getWidth());
        }
    }
    
    @Deprecated
    private void setIvMainGrayImage(BufferedImage image) {
        if (image.getWidth() > ivMainGray.getFitWidth() || image.getHeight() > ivMainGray.getFitHeight()) {
            ivMainGray.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivMainGray.setImage(SwingFXUtils.toFXImage(image, null));
            ivMainGray.setFitHeight(image.getHeight());
            ivMainGray.setFitWidth(image.getWidth());
        }
    }
    
    @Deprecated
    private void setIvMainGrayEqImage(BufferedImage image) {
        if (image.getWidth() > ivMainGrayEq.getFitWidth() || image.getHeight() > ivMainGrayEq.getFitHeight()) {
            ivMainGrayEq.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivMainGrayEq.setImage(SwingFXUtils.toFXImage(image, null));
            ivMainGrayEq.setFitHeight(image.getHeight());
            ivMainGrayEq.setFitWidth(image.getWidth());
        }
    }
    
    @Deprecated
    private void setIvMainBwImage(BufferedImage image) {
        if (image.getWidth() > ivMainBw.getFitWidth() || image.getHeight() > ivMainBw.getFitHeight()) {
            ivMainBw.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivMainBw.setImage(SwingFXUtils.toFXImage(image, null));
            ivMainBw.setFitHeight(image.getHeight());
            ivMainBw.setFitWidth(image.getWidth());
        }
    }
//histogram

    @Deprecated
    private void setIvHistogram(BufferedImage image) {
        if (image.getWidth() > ivHistogram.getFitWidth() || image.getHeight() > ivHistogram.getFitHeight()) {
            ivHistogram.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivHistogram.setImage(SwingFXUtils.toFXImage(image, null));
            ivHistogram.setFitHeight(image.getHeight());
            ivHistogram.setFitWidth(image.getWidth());
        }
    }
    
    @Deprecated
    private void createGraph() {
        if (chartHistogram.getData().size() == 0) {
            XYChart.Series seriesR = new XYChart.Series();
            XYChart.Series seriesG = new XYChart.Series();
            XYChart.Series seriesB = new XYChart.Series();
            XYChart.Series seriesGray = new XYChart.Series();
            
            ObservableList dataR = seriesR.getData();
            ObservableList dataG = seriesG.getData();
            ObservableList dataB = seriesB.getData();
            ObservableList dataGray = seriesGray.getData();
            
            for (int i = 0; i < 256; i++) {
                dataR.add(new XYChart.Data(i, gambar.histogram.r[i]));
                dataG.add(new XYChart.Data(i, gambar.histogram.g[i]));
                dataB.add(new XYChart.Data(i, gambar.histogram.b[i]));
                dataGray.add(new XYChart.Data(i, gambar.histogram.gray[i]));
            }
            chartHistogram.getData().add(seriesR);
            chartHistogram.getData().add(seriesG);
            chartHistogram.getData().add(seriesB);
            chartHistogram.getData().add(seriesGray);
            
            seriesR.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(255, 0, 0, 1.0);");
            seriesG.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(0, 255, 0, 1.0);");
            seriesB.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(0, 0, 255, 1.0);");
            seriesGray.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(128, 128, 128, 1.0);");
        }
    }
//ekualisasi histogram

    @Deprecated
    private void setIvHistogramEq(BufferedImage image) {
        if (image.getWidth() > ivEqHistogram.getFitWidth() || image.getHeight() > ivEqHistogram.getFitHeight()) {
            ivEqHistogram.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivEqHistogram.setImage(SwingFXUtils.toFXImage(image, null));
            ivEqHistogram.setFitHeight(image.getHeight());
            ivEqHistogram.setFitWidth(image.getWidth());
        }
    }
    
    @Deprecated
    private void createEqGraph(int from, int to) {
        if (chartEqHistogram.getData().size() == 0) {
            gambar.equalize(from, to);
            gambar.updateBufferedImage();
//            setIvHistogramEq(gambar.biEqualized);
            
            XYChart.Series seriesGray = new XYChart.Series();
            XYChart.Series seriesEq = new XYChart.Series();
            ObservableList dataGray = seriesGray.getData();
            ObservableList dataEq = seriesEq.getData();
            for (int i = 0; i < 256; i++) {
                dataGray.add(new XYChart.Data(i, gambar.histogram.gray[i]));
                dataEq.add(new XYChart.Data(i, gambar.histogram.equalized[i]));
            }
            chartEqHistogram.getData().add(seriesGray);
            chartEqHistogram.getData().add(seriesEq);
            seriesGray.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(255, 128, 128, 1.0);");
            seriesEq.getNode().lookup(".chart-series-line").setStyle("-fx-stroke-width: 1px;-fx-stroke: rgba(0, 0, 0, 1.0);");
        }
    }
    
    @FXML
    public void sliderListener() {
        int valFrom = (int) Math.round(sliderEqHistogramFrom.getValue());
        int valTo = (int) Math.round(sliderEqHistogramTo.getValue());
        int from = valFrom <= valTo ? valFrom : valTo;
        int to = valFrom <= valTo ? valTo : valFrom;
        
        chartEqHistogram.getData().clear();
        openTabEkualisasiHistogram(from, to);
    }
//chain code

    @Deprecated
    private void setIvChainCode(BufferedImage image) {
        if (image.getWidth() > ivChainCode.getFitWidth() || image.getHeight() > ivChainCode.getFitHeight()) {
            ivChainCode.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivChainCode.setImage(SwingFXUtils.toFXImage(image, null));
            ivChainCode.setFitHeight(image.getHeight());
            ivChainCode.setFitWidth(image.getWidth());
        }
    }
    
    @Deprecated
    private void setIvChainCodeBolong(BufferedImage image) {
        if (image.getWidth() > ivChainCodeBolong.getFitWidth() || image.getHeight() > ivChainCodeBolong.getFitHeight()) {
            ivChainCodeBolong.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivChainCodeBolong.setImage(SwingFXUtils.toFXImage(image, null));
            ivChainCodeBolong.setFitHeight(image.getHeight());
            ivChainCodeBolong.setFitWidth(image.getWidth());
        }
    }
    
    private void setTextChainCode() {
        String chain = "";
        int q = 1;
        for (List<String> x : chainData) {
            if (x.size() > 0) {
                chain += "Object : " + q + "\n";
                Iterator j = x.iterator();
                while (j.hasNext()) {
                    chain += j.next() + ",";
                }
                chain += "\n";
                q++;
            }
        }
        textChainCode.setText(chain);
    }
//kode belok

    @Deprecated
    private void setIvKodeBelok(BufferedImage image) {
        if (image.getWidth() > ivKodeBelok.getFitWidth() || image.getHeight() > ivKodeBelok.getFitHeight()) {
            ivKodeBelok.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivKodeBelok.setImage(SwingFXUtils.toFXImage(image, null));
            ivKodeBelok.setFitHeight(image.getHeight());
            ivKodeBelok.setFitWidth(image.getWidth());
        }
    }
    
    @Deprecated
    private void setIvKodeBelokBolong(BufferedImage image) {
        if (image.getWidth() > ivKodeBelokBolong.getFitWidth() || image.getHeight() > ivKodeBelokBolong.getFitHeight()) {
            ivKodeBelokBolong.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivKodeBelokBolong.setImage(SwingFXUtils.toFXImage(image, null));
            ivKodeBelokBolong.setFitHeight(image.getHeight());
            ivKodeBelokBolong.setFitWidth(image.getWidth());
        }
    }
    
    private void setTextKodeBelok() {
        String belok = "";
        int q = 1;
        for (List<String> x : belokData) {
            if (x.size() > 0) {
                belok += "Object : " + q + "\n";
                Iterator i = x.iterator();
                while (i.hasNext()) {
                    belok += i.next() + ",";
                }
                belok += "\n";
                q++;
            }
        }
        textKodeBelok.setText(belok);
    }
//tulang

    @Deprecated
    private void setIvTulangBw(BufferedImage image) {
        if (image.getWidth() > ivTulangBw.getFitWidth() || image.getHeight() > ivTulangBw.getFitHeight()) {
            ivTulangBw.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivTulangBw.setImage(SwingFXUtils.toFXImage(image, null));
            ivTulangBw.setFitHeight(image.getHeight());
            ivTulangBw.setFitWidth(image.getWidth());
        }
    }
    
    @Deprecated
    private void setIvTulangResult(BufferedImage image) {
        if (image.getWidth() > ivTulangResult.getFitWidth() || image.getHeight() > ivTulangResult.getFitHeight()) {
            ivTulangResult.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivTulangResult.setImage(SwingFXUtils.toFXImage(image, null));
            ivTulangResult.setFitHeight(image.getHeight());
            ivTulangResult.setFitWidth(image.getWidth());
        }
    }
    
    private void setTextTulangChain(List<List<String>> data) {
        List<String> cabang = data.get(0);
        data.remove(0);
        String tulangChain = "";
        Iterator i = cabang.iterator();
        tulangChain += "Cabang : \n";
        while (i.hasNext()) {
            tulangChain += i.next();
        }
        tulangChain += "\n\n";
        String tulangChainBelok = Operation.getOp().getChainBelokHuruf(data);
        tulangChain += "Chain Code : \n" + tulangChainBelok;
        textTulangChain.setText(tulangChain);
        setTextHuruf(td.get(tulangChainBelok));
    }

    //huruf
    @Deprecated
    private void setIvHuruf(BufferedImage image) {
        if (image.getWidth() > ivHuruf.getFitWidth() || image.getHeight() > ivHuruf.getFitHeight()) {
            ivHuruf.setImage(SwingFXUtils.toFXImage(image, null));
        } else {
            ivHuruf.setImage(SwingFXUtils.toFXImage(image, null));
            ivHuruf.setFitHeight(image.getHeight());
            ivHuruf.setFitWidth(image.getWidth());
        }
    }
    
    private void setTextHuruf(String huruf) {
        textHuruf.setText(huruf);
    }
    
    private void setMap() {
        td.put("7,+,0,-,7,7,7,-,6,+,7,7,7,7,7,7,+,0,-,7,+,0,-,7,+,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,+,1,-,0,+,1,-,0,+,1,1,1,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,-,7,-,6,6,6,+,7,-,6,6,+,7,7,7,7,+,0,-,7,7,+,0,-,7,+,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,-,7,+,0,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,6,2,2,2,-,1,+,2,2,2,-,1,+,2,-,1,+,2,-,1,-,0,+,1,1,-,0,0,+,1,-,0,+,1,-,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,0,0,-,7,+,0,-,7,+,0,-,7,+,0,0,-,7,7,7,+,0,0,-,7,+,0,-,7,+,0,0,-,7,-,7,7,7,7,", "a");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,7,+,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,7,+,0,0,-,7,+,0,-,7,+,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,+,1,-,0,+,1,1,1,1,+,2,-,1,1,+,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,+,3,-,2,2,2,+,3,-,2,2,2,2,+,3,-,2,2,+,3,3,-,2,+,3,-,2,+,3,+,6,-,3,-,2,+,3,3,3,+,4,-,3,+,4,-,3,+,4,-,3,+,6,-,3,+,4,4,-,3,+,4,4,4,4,4,+,5,-,4,4,4,4,+,5,-,2,+,5,5,-,4,+,5,5,-,2,+,5,+,6,-,4,4,+,5,-,4,4,+,5,-,4,+,5,5,-,2,+,3,+,7,-,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,1,1,", "b");
        td.put("7,-,6,6,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,7,7,7,7,7,7,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,+,1,1,-,0,+,1,1,+,2,-,1,+,2,-,1,1,+,2,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,1,1,1,1,1,1,1,-,0,+,1,-,0,0,+,1,-,0,0,0,+,1,-,0,0,0,0,0,0,0,0,-,7,+,0,0,0,-,7,+,0,-,7,+,0,0,-,7,7,7,7,7,7,-,6,6,+,7,7,-,6,6,+,7,7,7,7,1,1,1,1,7,", "c");
        td.put("7,-,6,6,6,6,6,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,7,7,7,7,7,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,0,0,0,0,0,0,0,0,0,+,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,+,2,-,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,0,-,7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,1,1,+,2,-,1,1,1,1,-,0,+,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,0,0,0,0,0,0,-,7,+,0,0,0,0,-,7,+,0,-,7,7,+,0,-,7,7,+,0,-,7,+,0,0,-,7,+,0,-,7,+,0,+,1,+,2,2,2,2,1,7,7,7,7,2,2,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,+,3,-,2,2,", "d");
        td.put("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,6,6,6,6,6,6,6,-,5,+,6,6,6,6,6,-,5,+,6,-,5,+,6,-,5,+,6,-,5,-,6,+,7,-,6,6,6,+,7,-,6,6,6,6,6,6,+,7,-,6,+,7,-,6,6,+,7,7,-,6,+,7,7,7,7,7,7,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,+,1,-,0,+,1,1,1,1,6,2,2,2,2,-,1,+,2,2,2,2,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,1,1,1,1,1,-,0,+,1,1,-,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,-,7,+,0,0,-,7,+,0,-,7,7,+,0,-,7,7,-,6,+,7,7,7,-,6,+,7,7,+,7,1,7,7,", "e");
        td.put("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,-,6,6,6,6,6,6,6,6,6,6,6,6,+,7,-,6,+,7,7,-,6,+,7,7,+,0,0,-,7,+,0,-,7,+,0,0,0,0,0,0,0,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,", "f");
        td.put("7,-,6,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,7,7,7,7,7,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,+,1,-,0,+,1,-,0,0,+,1,1,-,0,+,1,1,1,-,0,0,+,1,-,0,+,1,-,0,+,1,-,0,-,6,+,7,-,6,6,6,6,6,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,1,+,2,-,1,1,1,1,-,0,+,1,1,-,0,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,-,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,-,7,+,0,0,-,6,6,6,6,6,6,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,-,5,+,6,6,6,6,6,1,1,-,0,+,2,-,1,+,2,-,1,1,1,1,1,-,0,+,1,-,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,-,7,+,0,-,7,+,0,-,7,7,7,+,0,-,6,+,7,-,6,+,7,7,-,6,+,7,-,6,6,6,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,1,1,1,7,7,7,", "g");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,-,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,0,-,7,7,7,+,0,-,7,+,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,+,1,-,0,0,0,+,1,-,0,0,+,1,1,-,0,+,1,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,-,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,1,", "h");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,", "i");
        td.put("7,+,0,0,0,0,0,0,0,-,7,+,0,-,7,+,0,-,7,-,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,2,", "j");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,-,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,0,0,0,-,7,+,0,0,-,7,7,-,6,6,+,7,-,6,6,+,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,+,0,-,7,+,0,0,-,7,+,0,0,-,7,3,-,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,1,-,0,+,1,-,0,0,+,1,-,0,+,7,7,7,7,7,", "k");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,", "l");
        td.put("2,2,2,2,1,-,0,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,+,1,-,0,0,0,+,1,-,0,+,1,1,1,1,+,2,-,1,+,2,2,2,-,1,-,0,0,0,-,7,7,+,0,-,7,+,0,-,7,7,7,7,7,+,0,-,7,7,+,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,+,1,-,0,0,+,1,-,0,+,1,-,0,+,1,1,1,+,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,-,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,", "m");
        td.put("2,2,2,2,1,-,0,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,0,+,1,-,0,0,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,1,+,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,3,-,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,", "n");
        td.put("7,-,6,6,6,6,6,6,6,+,7,-,6,+,7,-,6,+,7,-,6,6,+,7,7,7,7,7,7,7,7,7,+,0,-,7,+,0,-,7,+,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,+,1,-,0,+,1,-,0,+,1,1,1,-,0,+,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,+,3,-,2,2,2,+,3,+,6,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,-,1,+,2,-,1,+,2,-,1,+,2,2,-,1,1,1,1,1,1,1,1,1,-,0,+,1,-,0,0,+,1,-,0,0,0,+,1,-,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,-,6,+,7,7,7,-,6,+,7,-,6,+,7,-,6,7,7,7,7,7,1,1,1,1,1,7,", "o");
        td.put("2,2,2,2,-,1,-,0,0,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,1,1,1,1,+,2,-,1,+,2,-,1,1,+,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,+,3,-,2,2,2,+,3,-,2,2,2,2,2,+,3,+,7,-,6,6,6,6,6,6,6,6,6,6,6,6,+,7,-,6,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,-,1,1,-,0,0,+,1,-,0,0,+,1,-,0,0,+,1,1,-,0,+,1,-,0,+,1,-,0,0,0,0,+,1,-,0,0,0,0,0,-,7,+,0,0,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,7,7,7,7,-,6,+,7,-,6,+,7,3,-,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,1,1,1,7,", "p");
        td.put("7,-,6,6,6,6,6,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,7,7,7,7,7,7,7,+,0,-,7,+,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,+,1,1,-,0,+,1,+,2,-,1,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,0,-,6,6,6,6,6,6,6,6,6,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,1,+,2,-,1,1,1,1,1,1,1,-,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,0,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,-,7,7,+,0,-,7,7,+,0,0,-,7,+,0,-,7,+,0,-,7,+,0,+,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,7,7,1,1,1,7,2,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,+,3,-,2,", "q");
        td.put("2,2,2,2,2,-,1,-,0,0,-,7,+,0,-,7,+,0,-,7,7,7,7,7,+,0,-,7,7,+,0,0,-,7,+,0,0,0,0,0,7,3,-,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,", "r");
        td.put("2,-,1,+,2,-,1,+,2,-,1,1,1,-,0,+,1,1,-,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,7,7,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,6,6,6,-,5,+,6,6,-,5,+,6,-,5,-,2,+,5,5,-,2,+,5,5,-,4,+,5,-,4,4,+,5,-,4,4,+,5,-,4,4,+,5,-,4,4,+,5,-,2,+,5,-,4,4,+,5,-,2,+,5,-,4,4,+,5,-,4,4,+,5,-,4,4,+,5,-,2,+,5,5,-,2,+,5,5,5,+,6,-,5,-,7,-,6,6,6,+,7,7,+,0,-,7,7,7,+,0,-,7,+,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,+,1,-,0,+,1,1,1,+,2,-,1,+,6,2,2,2,2,2,2,2,7,1,1,7,", "s");
        td.put("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,-,6,6,+,7,-,6,+,7,-,6,+,7,-,6,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,-,1,+,2,-,1,1,-,0,+,1,-,0,0,0,+,1,-,0,-,", "t");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,-,1,+,2,-,1,1,+,2,-,1,1,-,0,+,1,1,-,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,-,7,+,0,-,7,7,+,0,-,7,7,7,+,0,0,-,7,+,0,-,7,+,0,-,7,+,1,+,2,2,2,2,2,2,2,7,-,6,6,6,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,", "u");
        td.put("2,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,-,0,+,1,-,0,0,0,0,-,7,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,2,", "v");
        td.put("2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,-,0,+,1,-,0,+,1,-,7,+,0,-,7,+,0,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,+,0,-,7,7,+,0,0,0,+,1,1,-,0,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,-,0,+,1,1,-,0,0,-,7,+,0,-,7,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,7,3,-,2,", "w");
        td.put("7,+,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,+,0,-,7,7,7,+,1,1,1,-,0,+,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,1,-,0,0,+,1,-,0,+,1,-,0,+,1,-,0,0,+,1,1,+,2,-,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,-,0,+,1,1,-,0,0,-,7,7,7,7,7,-,6,+,7,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,+,0,-,7,+,0,0,-,7,+,0,-,7,7,7,1,7,7,7,7,-,6,6,6,7,7,7,1,7,1,7,1,7,7,7,", "x");
        td.put("2,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,1,-,0,+,1,1,-,0,-,7,7,+,0,-,7,-,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,+,0,-,7,+,1,1,-,0,0,0,0,0,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,6,+,7,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,6,6,6,+,7,-,6,6,6,6,", "y");
        td.put("7,+,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,-,7,-,6,6,+,7,-,6,6,+,7,7,7,7,7,-,6,+,7,7,7,7,7,7,-,6,+,7,7,7,7,7,-,6,+,7,7,7,7,7,7,-,6,+,7,7,7,7,7,7,-,6,+,7,7,7,7,7,7,-,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,7,7,7,7,7,7,7,7,7,7,7,", "z");
        td.put("7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,+,7,-,6,6,6,6,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,7,7,7,7,+,0,-,7,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,0,0,+,1,-,0,0,+,1,-,0,+,1,1,-,0,+,1,-,0,+,1,1,-,0,+,1,1,1,1,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,2,-,1,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,+,3,+,6,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,-,1,+,2,2,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,-,1,1,+,2,-,1,+,2,-,1,1,1,1,1,1,1,1,1,1,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,0,+,1,-,0,0,-,7,+,0,0,0,0,0,0,0,0,0,-,7,+,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,7,7,7,7,7,7,7,-,6,+,7,7,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,6,+,7,-,6,6,6,6,6,1,1,7,1,", "0");
        td.put("7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,+,0,-,7,+,0,-,7,7,+,0,-,7,7,+,0,-,7,7,+,0,-,7,7,7,+,0,-,7,7,7,+,0,0,-,7,+,0,0,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,0,+,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,7,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,6,+,7,-,6,6,2,", "1");
        td.put("7,+,0,-,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,+,1,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,7,-,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,7,-,6,+,7,-,6,+,7,7,7,7,-,6,+,7,7,7,+,0,-,7,7,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,0,+,1,-,0,0,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,1,-,0,+,1,1,1,1,1,1,1,+,2,-,1,1,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,-,1,+,2,2,2,2,-,1,+,2,2,2,2,2,2,-,1,+,2,2,2,2,2,+,3,-,2,2,2,2,2,2,+,3,-,2,2,2,+,3,-,2,2,2,+,3,-,2,+,3,-,2,+,3,-,2,+,3,-,2,+,3,-,2,+,3,-,2,+,3,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,+,7,7,7,7,7,-,6,+,7,7,7,7,7,7,7,7,7,7,7,7,7,+,0,-,7,7,7,7,7,7,7,7,+,0,-,7,7,7,7,7,7,+,0,-,7,7,7,7,7,+,0,-,7,7,7,7,7,7,+,0,-,7,7,7,7,7,7,7,7,7,+,0,-,7,7,7,7,7,7,7,7,7,7,-,6,+,7,7,7,7,7,-,6,+,7,7,7,7,-,6,-,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,1,1,1,1,1,", "2");
        td.put("2,-,1,+,2,-,1,+,2,2,2,-,1,1,+,2,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,1,1,1,1,1,1,-,0,+,1,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,+,1,-,0,0,0,0,0,0,0,-,7,+,0,0,0,0,0,0,0,0,-,7,+,0,0,-,7,+,0,0,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,+,0,-,7,7,7,7,7,7,7,7,7,7,-,6,+,7,7,-,6,+,7,-,6,6,+,7,-,6,+,7,7,-,6,6,6,6,+,7,-,6,6,6,6,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,-,5,+,6,6,6,6,6,-,5,+,6,6,6,-,5,+,6,-,5,+,6,-,5,+,6,-,5,+,6,-,5,-,7,-,6,+,7,-,6,6,+,7,7,-,6,+,7,-,6,+,7,-,6,+,7,7,-,6,+,7,7,7,7,7,7,7,7,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,0,0,0,+,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,1,1,-,0,+,1,+,2,-,1,1,1,1,1,+,2,-,1,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,2,2,-,1,+,2,2,2,2,2,2,-,1,+,3,-,2,2,2,2,2,2,+,3,-,2,2,2,2,+,3,+,1,1,1,0,-,7,7,+,0,-,7,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,-,7,-,6,+,7,-,6,+,7,-,6,+,7,7,7,+,0,-,7,+,0,-,7,7,7,7,7,7,7,-,6,+,7,-,6,+,7,7,-,6,6,6,+,6,1,1,+,2,-,1,+,2,-,1,-,0,0,+,1,-,0,+,1,-,0,0,+,1,1,1,-,0,+,1,-,0,+,1,1,1,1,1,+,2,-,1,7,1,7,1,1,1,", "3");
        td.put("7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,7,+,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,-,7,+,0,0,0,+,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,", "4");
        td.put("7,+,0,-,7,7,7,+,0,-,6,+,7,7,-,6,6,+,7,+,0,0,-,7,+,0,0,0,-,7,+,0,0,0,-,7,+,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,-,7,7,+,0,0,0,-,7,+,0,0,0,-,7,+,0,0,0,0,-,7,+,0,0,0,0,0,0,0,0,-,7,+,0,0,0,+,1,-,0,0,0,0,+,1,-,0,0,0,0,0,+,1,-,0,+,1,-,0,0,+,1,-,0,+,1,1,-,0,+,1,-,0,+,1,-,0,+,1,1,1,1,1,1,1,1,+,2,-,1,1,1,+,2,-,1,+,2,-,1,1,+,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,+,3,-,2,2,2,2,2,2,+,3,-,2,2,+,3,-,2,2,2,2,+,3,-,2,+,3,-,2,2,+,3,+,7,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,1,+,2,2,-,1,1,1,1,+,2,-,1,1,1,1,1,1,1,-,0,+,1,1,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,-,7,+,0,0,-,7,+,0,0,0,-,7,+,0,-,7,+,0,0,-,7,+,0,-,7,7,+,0,-,7,7,+,0,-,7,7,7,7,7,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,-,6,+,7,-,6,6,6,6,6,6,6,6,6,6,+,7,-,6,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,+,7,+,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,2,2,2,2,2,1,1,1,1,1,1,1,7,", "5");
        td.put("7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,-,6,6,+,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,7,7,7,7,7,7,7,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,0,0,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,+,1,1,-,0,+,1,1,1,1,1,1,1,1,+,2,-,1,1,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,6,2,2,2,2,-,1,-,0,+,1,-,0,-,7,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,-,7,-,6,+,7,7,7,7,7,+,0,-,6,+,7,7,+,0,-,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,0,0,0,-,7,+,0,0,0,0,0,0,0,-,7,+,0,0,0,0,0,+,1,-,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,+,1,-,0,0,+,1,-,0,+,1,1,-,0,+,1,1,1,-,0,+,1,1,1,1,1,1,1,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,+,3,-,2,2,2,2,2,2,2,+,3,-,2,2,2,2,2,+,3,-,2,+,3,-,2,2,+,3,-,2,2,2,+,3,+,7,-,6,6,6,6,6,1,+,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,-,1,1,+,2,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,1,1,1,1,-,0,+,1,1,1,-,0,+,1,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,0,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,+,1,-,0,0,0,-,7,+,0,0,0,0,0,0,0,0,-,7,+,0,0,-,7,+,0,0,0,-,7,+,0,-,7,+,0,0,-,7,7,+,0,-,7,7,+,0,-,7,7,7,7,7,7,7,7,-,6,+,7,7,-,6,+,7,-,6,+,7,7,-,7,7,7,1,7,7,7,7,1,1,1,1,7,1,7,", "6");
        td.put("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,7,+,0,0,-,0,0,-,7,7,7,7,-,6,6,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,6,6,6,+,7,-,6,6,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,7,-,6,+,7,-,6,+,7,-,6,+,7,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,7,-,6,+,7,7,7,-,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,7,7,7,7,", "7");
        td.put("7,-,6,6,6,6,6,+,7,-,6,+,7,-,6,6,6,+,7,-,6,+,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,7,7,7,7,7,+,0,-,7,+,0,-,7,7,+,0,0,-,7,+,0,-,7,+,0,-,7,7,-,6,+,7,7,-,6,+,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,-,6,+,7,7,-,6,+,7,-,6,+,7,+,0,-,7,+,0,-,7,7,7,+,0,-,7,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,6,6,+,7,7,-,6,6,6,6,+,7,-,6,6,6,6,6,6,+,7,-,5,+,6,6,6,6,6,6,-,5,+,6,6,6,6,-,5,+,6,-,5,+,6,6,-,5,+,6,6,-,5,-,6,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,1,1,1,1,1,1,1,-,0,+,1,1,-,0,+,1,-,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,-,7,+,0,0,-,7,+,0,0,0,-,7,+,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,7,7,+,0,-,7,7,7,7,7,+,0,-,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,6,6,6,6,+,7,-,6,6,-,5,+,6,6,6,6,6,6,6,-,5,+,6,6,-,5,+,6,6,6,-,5,+,6,6,-,5,+,6,6,-,5,+,6,-,5,5,5,+,6,-,5,-,7,-,6,6,6,6,+,7,-,6,6,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,7,7,7,7,7,7,7,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,0,0,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,1,-,0,+,1,1,1,1,-,0,+,1,+,2,-,1,1,+,2,-,6,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,-,1,1,+,2,2,-,1,1,+,2,-,1,-,0,+,2,-,1,1,1,1,1,-,0,+,1,1,-,0,+,1,-,0,+,1,1,+,2,-,1,1,+,2,-,1,+,1,1,1,1,7,7,1,7,7,1,1,+,2,-,1,+,2,-,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,1,-,0,+,1,1,1,1,1,-,0,+,1,+,2,+,7,7,7,", "8");
        td.put("7,-,6,6,+,7,-,6,6,6,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,7,7,7,7,-,6,+,7,7,7,7,7,7,7,7,+,0,-,7,7,+,0,-,7,7,+,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,0,0,+,1,-,0,0,0,+,1,-,0,+,1,-,0,0,+,1,-,0,+,1,1,-,0,+,1,1,-,0,+,1,1,1,1,1,+,2,-,1,1,1,1,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,+,3,-,2,2,2,2,2,2,2,2,2,2,2,2,2,2,+,3,-,2,2,2,2,2,2,+,3,-,2,2,2,2,2,+,3,+,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,-,1,+,2,-,1,1,+,2,-,1,1,1,1,1,1,1,1,1,1,1,-,0,+,1,-,0,+,1,1,-,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,-,7,+,0,-,7,+,0,0,-,7,+,0,-,7,7,+,0,-,7,+,0,-,7,7,7,7,7,7,7,7,7,7,+,0,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,+,0,-,7,-,2,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,-,1,1,+,2,-,1,+,2,-,1,1,1,1,1,-,0,+,1,1,1,-,0,+,1,-,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,0,0,0,0,0,0,0,+,1,-,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,-,7,+,0,0,0,-,7,+,0,0,-,7,7,+,0,-,7,+,0,-,7,7,+,0,-,7,7,7,7,7,+,0,-,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,7,-,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,1,7,1,1,1,1,1,1,1,1,1,1,7,7,7,", "9");
        td.put("0,0,-,7,7,+,0,-,7,+,0,-,7,+,0,-,7,7,+,0,-,7,+,0,-,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,+,0,-,7,+,0,-,7,+,1,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,-,7,+,0,+,1,-,0,+,1,-,0,+,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,7,-,6,6,+,7,-,6,6,6,6,6,+,7,-,6,6,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,+,0,-,7,+,0,-,7,7,+,0,0,0,0,0,0,+,1,-,0,+,1,-,0,+,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,2,2,-,1,+,2,2,2,2,2,-,1,+,2,2,+,", "A");
        td.put("7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,0,+,1,1,-,0,+,1,1,1,1,+,2,-,1,+,2,2,2,-,1,+,2,-,1,+,2,2,2,2,2,+,3,-,2,2,2,+,3,-,2,2,+,3,-,2,+,3,+,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,-,6,+,7,7,-,6,+,7,+,0,-,7,7,-,4,-,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,-,7,+,0,-,7,7,7,7,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,-,5,+,6,-,5,+,6,6,-,5,+,6,-,5,-,6,1,1,-,0,+,1,-,0,+,1,-,0,+,1,1,1,7,7,7,", "B");
        td.put("7,-,6,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,7,-,6,+,7,-,6,+,7,-,6,+,7,7,7,7,7,7,7,7,7,+,0,-,7,7,+,0,-,7,+,0,0,-,7,+,0,0,0,0,-,7,+,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,0,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,1,1,1,1,1,1,1,+,2,-,1,+,2,2,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,-,1,1,+,2,-,1,+,2,-,1,1,1,+,2,-,1,-,0,+,1,1,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,-,7,+,0,-,7,+,0,-,7,+,0,-,7,7,+,0,-,7,7,7,7,7,-,6,+,7,7,-,6,+,7,-,6,6,+,7,-,6,+,7,7,1,7,7,7,7,7,1,7,7,", "C");
        td.put("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,+,1,-,0,+,1,-,0,0,+,1,1,-,0,+,1,1,-,0,+,1,1,1,+,2,-,1,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,+,3,-,2,2,2,2,2,2,2,2,2,2,+,3,+,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,7,7,7,7,7,7,7,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,1,1,7,", "D");
        td.put("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,", "E");
        td.put("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,", "F");
        td.put("7,-,6,6,+,7,-,6,6,6,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,7,7,7,+,0,-,7,+,0,-,7,7,+,0,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,+,1,1,-,0,+,1,1,1,1,1,1,1,+,2,-,1,+,2,-,1,+,2,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,-,1,+,2,-,1,1,+,2,-,1,1,1,1,1,1,1,1,-,0,+,1,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,0,0,+,1,-,0,0,0,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,0,0,-,7,+,0,0,-,7,+,0,-,7,+,0,0,-,7,7,+,0,-,7,+,0,-,7,7,7,-,6,+,7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,-,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,1,7,", "G");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,", "H");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,", "I");
        td.put("7,3,-,2,2,2,2,2,2,2,-,1,+,2,-,1,1,+,2,-,1,1,1,1,1,-,0,+,1,-,0,0,0,0,0,+,1,-,0,0,0,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,-,7,+,0,-,7,7,7,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,1,7,7,", "J");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,0,0,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,-,7,7,7,7,7,7,+,0,0,-,7,+,0,0,-,7,+,0,0,+,1,1,+,2,-,1,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,1,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,+,7,-,6,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,-,6,6,+,7,-,6,+,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,+,0,0,-,7,+,0,0,-,7,+,0,0,-,6,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,1,1,1,1,1,", "K");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,", "L");
        td.put("7,7,7,+,0,0,0,+,1,1,-,0,+,1,-,0,+,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,-,0,+,1,-,0,0,0,0,-,7,7,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,+,1,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,2,", "M");
        td.put("7,7,7,+,0,+,1,-,0,+,1,-,0,+,1,-,0,0,+,1,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,-,0,+,1,-,0,+,1,-,0,0,+,1,-,0,0,-,7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,+,2,2,2,2,+,1,+,2,", "N");
        td.put("7,-,6,6,6,6,+,7,-,6,6,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,7,-,6,+,7,7,7,7,7,7,+,0,-,7,7,+,0,-,7,+,0,-,7,+,0,-,7,+,0,0,-,7,+,0,-,7,+,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,0,0,+,1,-,0,+,1,-,0,0,+,1,1,-,0,+,1,1,-,0,+,1,1,1,1,1,+,2,-,1,1,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,2,-,1,+,2,-,1,+,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,2,+,3,-,2,2,2,2,2,2,2,2,+,3,-,2,2,+,3,-,2,2,+,3,+,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,-,1,+,2,-,1,1,+,2,-,1,+,2,-,1,+,2,-,1,1,1,1,1,1,1,1,1,-,0,+,1,1,-,0,+,1,-,0,+,1,-,0,0,+,1,-,0,0,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,7,7,+,0,-,7,7,7,+,0,-,7,7,7,-,6,+,7,7,7,-,6,6,+,7,-,6,+,1,7,1,7,", "O");
        td.put("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,+,1,-,0,+,1,1,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,+,3,-,2,2,+,3,+,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,-,7,+,0,0,0,0,-,7,+,0,0,-,7,7,+,0,-,7,7,-,6,+,7,-,6,+,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,1,7,", "P");
        td.put("7,-,6,6,6,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,7,7,-,6,+,7,7,+,0,-,7,7,7,7,+,0,-,7,+,0,-,7,7,+,0,0,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,+,1,-,0,0,0,+,1,-,0,0,+,1,-,0,+,1,-,0,+,1,1,-,0,+,1,1,1,1,1,1,1,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,-,1,+,2,2,2,-,1,+,2,-,1,+,2,2,2,-,1,+,2,2,2,2,2,2,2,2,2,-,1,+,2,+,3,-,2,2,2,2,2,2,2,2,+,3,-,2,2,2,2,2,+,3,-,2,+,3,-,2,2,2,+,3,+,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,+,2,2,2,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,1,1,1,-,0,+,1,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,0,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,0,-,7,+,0,-,7,-,6,+,7,7,7,+,0,0,0,0,0,0,0,-,7,-,6,+,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,-,6,+,1,7,+,1,-,0,+,1,-,0,+,1,-,0,+,1,1,+,2,-,1,1,1,+,1,1,+,2,-,1,1,1,-,0,+,1,1,-,0,+,1,1,-,0,+,1,-,7,1,1,1,", "Q");
        td.put("0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,+,1,1,1,1,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,2,2,2,2,2,2,2,2,+,3,-,2,+,3,-,2,2,+,3,-,2,+,3,+,6,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,-,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,-,7,+,0,0,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,7,7,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,+,2,2,-,1,+,2,-,1,+,2,-,1,1,-,0,+,1,1,1,+,2,-,1,1,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,+,2,-,1,1,+,2,-,1,-,0,0,+,1,-,0,+,1,-,0,+,1,1,", "R");
        td.put("2,-,1,+,2,2,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,1,1,-,0,+,1,1,-,0,+,1,-,0,0,+,1,-,0,0,+,1,-,0,0,0,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,0,0,-,7,+,0,0,-,7,+,0,-,7,7,+,0,-,7,+,0,-,7,7,-,6,+,7,7,-,6,+,7,-,6,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,6,-,5,+,6,6,-,5,+,6,-,5,-,2,+,5,+,6,-,5,-,2,+,5,+,6,-,4,+,5,-,4,+,5,5,-,4,+,5,-,4,4,+,5,-,4,4,+,5,-,4,4,4,+,5,-,4,4,4,+,5,-,4,4,4,+,5,-,4,4,4,+,5,-,4,4,+,5,-,4,4,+,5,-,4,4,+,5,-,2,+,5,-,4,+,5,-,4,4,+,5,5,-,2,+,5,5,5,-,2,+,5,+,6,6,-,5,-,7,-,6,6,6,6,6,+,7,-,6,+,7,-,6,+,7,-,6,+,7,7,7,+,0,-,7,7,+,0,-,7,+,0,0,-,7,+,0,0,-,7,+,0,0,0,-,7,+,0,0,0,0,0,0,0,0,0,0,0,0,+,1,-,0,0,0,+,1,-,0,0,0,+,1,-,0,+,1,-,0,+,1,1,-,0,+,1,1,1,1,+,2,-,1,+,2,-,1,+,2,2,2,6,-,2,2,-,1,1,1,7,", "S");
        td.put("0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,", "T");
        td.put("2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,+,2,2,2,2,-,1,+,2,2,2,2,2,-,1,+,2,2,-,1,1,+,2,-,1,1,1,1,1,1,-,0,+,1,1,-,0,+,1,-,0,0,+,1,-,0,0,0,0,0,+,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,-,7,+,0,0,0,0,-,7,+,0,0,0,-,7,+,0,-,7,+,0,-,7,7,7,7,7,7,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,6,6,6,6,+,7,-,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,1,1,1,1,7,", "U");
        td.put("2,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,2,-,1,+,2,-,1,+,2,2,2,-,1,1,-,0,+,1,-,0,0,0,-,7,+,0,-,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,+,7,-,6,6,+,7,-,6,6,+,7,-,6,6,+,7,7,+,0,-,7,+,0,-,7,+,6,", "V");
        td.put("2,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,-,0,+,1,1,-,0,0,-,7,7,+,0,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,7,+,0,-,7,7,+,0,+,1,-,0,+,1,1,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,-,1,+,2,2,2,-,1,+,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,2,-,1,+,2,2,2,2,-,1,1,1,-,0,+,1,-,0,-,7,7,+,0,-,6,+,7,-,6,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,-,6,6,6,6,+,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,6,+,7,-,6,6,6,+,7,", "W");
        td.put("0,0,-,7,+,0,0,-,7,+,0,-,7,+,0,-,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,7,-,6,+,7,7,7,-,6,+,7,7,7,+,0,-,7,7,7,+,0,+,1,1,1,-,0,+,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,-,0,+,1,-,0,+,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,0,+,1,-,0,+,1,-,0,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,-,0,+,1,1,1,-,0,-,7,7,7,7,7,7,-,6,+,7,7,7,-,6,+,7,7,7,-,6,+,7,7,7,-,6,+,7,7,7,-,6,+,7,7,7,-,6,+,7,7,7,-,6,+,7,7,7,-,6,+,7,7,7,7,7,+,0,0,-,7,+,0,-,1,1,1,1,7,7,7,7,-,6,6,6,7,7,1,7,1,7,1,7,1,1,7,7,1,1,7,1,7,1,7,1,", "X");
        td.put("1,-,0,+,1,-,0,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,+,2,-,1,1,+,2,-,1,1,+,2,-,1,+,2,-,1,+,2,-,1,1,1,-,0,+,1,-,0,0,-,7,+,0,-,7,+,0,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,-,6,+,7,7,-,6,+,7,7,-,6,+,7,7,7,+,0,-,7,+,0,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,7,7,7,", "Y");
        td.put("7,-,6,6,+,7,-,6,6,+,7,7,7,7,7,7,-,6,+,7,7,7,7,-,6,+,7,7,7,7,-,6,+,7,7,7,7,-,6,+,7,7,7,7,7,-,6,+,7,7,7,7,-,6,+,7,7,7,7,-,6,+,7,7,7,7,-,6,+,7,7,7,7,7,-,6,+,7,7,7,7,-,6,+,7,7,7,7,-,6,+,7,7,7,7,-,6,6,6,+,7,-,6,6,+,7,-,6,6,-,5,-,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,6,2,2,2,2,-,1,-,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,7,7,7,7,7,7,", "Z");
    }
}
