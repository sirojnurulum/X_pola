/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pola.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    @FXML
    Tab tabBuremin;
    @FXML
    ImageView ivBureminOri;
    @FXML
    ImageView ivBureminResult;
    @FXML
    Tab tabSobel;
    @FXML
    ImageView ivSobelOri;
    @FXML
    ImageView ivSobelResult;
    @FXML
    Tab tabHomogen8;
    @FXML
    ImageView ivHomogen8Ori;
    @FXML
    ImageView ivHomogen8Result;
    @FXML
    Tab tabEmboss;
    @FXML
    ImageView ivEmbossOri;
    @FXML
    ImageView ivEmbossResult;
    //
    File fileImageOri;
    Gambar gambar;
    List<List<String>> chainData, belokData, chainTulangData;
    List<String> cabangData;
    Huruf huruf;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (huruf == null) {
            huruf = new Huruf();
        }
        for (ImageView iv : new ImageView[]{
            ivMainOri, ivMainGray, ivMainGrayEq, ivMainBw, ivHistogram, ivEqHistogram, ivChainCode, ivChainCodeBolong, ivKodeBelok, ivKodeBelokBolong, ivTulangBw, ivTulangResult, ivHuruf,
            ivBureminOri, ivBureminResult, ivSobelOri, ivSobelResult, ivHomogen8Ori, ivHomogen8Result,
            ivEmbossOri, ivEmbossResult}) {
            iv.setUserData(new double[]{iv.getFitWidth(), iv.getFitHeight()});
        }

        setTabAccess(false);

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
            } else if (newValue == tabBuremin) {
                openTabBuremin();
            } else if (newValue == tabSobel) {
                openTabSobel();
            } else if (newValue == tabHomogen8) {
                openTabHomogen8();
            } else if (newValue == tabEmboss) {
                openTabEmboss();
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
        tabBuremin.setDisable(!active);
        tabSobel.setDisable(!active);
        tabHomogen8.setDisable(!active);
        tabEmboss.setDisable(!active);
    }
// <editor-fold defaultstate="collapsed" desc="rengse - ke deui ngomena">

    @FXML
    public void chooseImage() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG"), new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG"));
        fileImageOri = new FileChooser().showOpenDialog(main.getPrimaryStage());

        if (fileImageOri != null) {
            // reset
            chartHistogram.getData().clear();
            chartEqHistogram.getData().clear();
            chainData = null;
            belokData = null;
            chainTulangData = null;

            gambar = new Gambar(fileImageOri);

            setImageView();
            setTabAccess(true);

            // konvolusi: samar
            new Thread(() -> {
//                BufferedImage samar = Gambar.toBufferedImage(Konvolusi.samarkan(gambar.grayscale));
//                try {
//                    ImageIO.write(samar, "PNG", new File("D:\\samar.png"));
//                } catch (IOException ex) {
//                    Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
//                }
                setIvImage(ivMainGrayEq, Gambar.toBufferedImage(Konvolusi.samarkan(gambar.grayscale)));
            }).start();
            // konvolusi: sobel  
            new Thread(() -> {
//                BufferedImage sobel = Gambar.toBufferedImage(Konvolusi.sobel(gambar.grayscale));
//                try {
//                    ImageIO.write(sobel, "PNG", new File("D:\\sobel.png"));
//                } catch (IOException ex) {
//                    Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
//                }
                setIvImage(ivMainBw, Gambar.toBufferedImage(Konvolusi.sobel(gambar.grayscale)));
            }).start();
        } else {
            if (gambar == null) {
                setTabAccess(false);
            }
            main.showAlert("Error", "File Chooser", "No Picture Selected !!!", Alert.AlertType.INFORMATION);
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
//</editor-fold>

    private void setImageView() {
        setIvImage(ivMainOri, gambar.biOriginal);
        setIvImage(ivMainGray, gambar.biGrayscale);
        //setIvImage(ivMainGrayEq, gambar.biEqualized); // diganti dengan konvolusi samarkan
        //setIvImage(ivMainBw, gambar.biBinary); // diganti dengan konvolusi sobel
        setIvImage(ivHistogram, gambar.biOriginal);
        setIvImage(ivTulangBw, gambar.biOriginal);
    }
// <editor-fold defaultstate="collapsed" desc="rengse - ke deui ngomena">

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
            gambar.binarization();
            gambar.bolongin();
            gambar.updateBufferedImage();

            setIvImage(ivChainCode, gambar.biBinary);
            setIvImage(ivChainCodeBolong, gambar.biBolong);

            chainData = Operation.getOp().getChainCode(gambar.biBolong);
            textChainCode.setText(Operation.getOp().createTextChainCodeKodeBelok(chainData));
        }
    }

    private void openTabKodeBelok() {
        if (belokData == null) {
            openTabChainCode();

            setIvImage(ivKodeBelok, gambar.biBinary);
            setIvImage(ivKodeBelokBolong, gambar.biBolong);

            belokData = Operation.getOp().getKodeBelok(chainData);
            textKodeBelok.setText(Operation.getOp().createTextChainCodeKodeBelok(belokData));
        }
    }

    private void openTabTulang() {
        if (chainTulangData == null) {
            openTabChainCode();

            gambar.tulangin();
            gambar.updateBufferedImage();
            setIvImage(ivTulangResult, gambar.biTulang);
            setIvImage(ivHuruf, gambar.biTulang);

            List<List<String>> data = Operation.getOp().getChainCodeTulang(gambar.biTulang);
            cabangData = data.get(0);
            data.remove(0);
            chainTulangData = Operation.getOp().getKodeBelok(data);
            String tChainTulang = Operation.getOp().createTextChainCodeTulang(chainTulangData);
            textTulangChain.setText(Operation.getOp().createTextCabang(cabangData) + "\n\nChain Code : \n" + tChainTulang);
            textHuruf.setText(huruf.td.get(tChainTulang));
        }
    }

    private void openTabHuruf() {
        openTabTulang();
    }

//</editor-fold>
    private void openTabBuremin() {
        setIvImage(ivBureminOri, gambar.biOriginal);
        new Thread(() -> {
            setIvImage(ivBureminResult, Operation.getOp().buremin(gambar.biOriginal));
        }).start();
    }

    private void openTabHomogen8() {
        setIvImage(ivHomogen8Ori, gambar.biOriginal);
        new Thread(() -> {
            setIvImage(ivHomogen8Result, Operation.getOp().konvolusi(gambar.biOriginal, "homogen8"));
        }).start();
    }

    private void openTabSobel() {
        setIvImage(ivSobelOri, gambar.biOriginal);
        new Thread(() -> {
            setIvImage(ivSobelResult, Operation.getOp().konvolusi(gambar.biOriginal, "sobel"));
        }).start();
    }

    private void openTabEmboss() {
        setIvImage(ivEmbossOri, gambar.biOriginal);
        new Thread(() -> {
            setIvImage(ivEmbossResult, Operation.getOp().konvolusi(gambar.biGrayscale, "emboss"));
        }).start();
    }
}
