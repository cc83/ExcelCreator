package main.java.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.datasdownloading.HttpDownload;
import main.java.excelwriter.ExcelWriter;
import main.java.sort.Sheets;
import main.java.utils.FileType;
import main.java.utils.Utils;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements IMainFrame {

    private List<SettingsChoicePanel> panels;
    private int currentPanel = 0;
    private NavigationPanel np;
    private BackgroundPanel backgroundPanel;
    private boolean download;

    public static int progress = 0;

    public static final int WINDOW_HEIGHT = 500;

    public static final int WINDOW_WIDTH = 600;

    private static HttpDownload session;

    private static boolean isRankings;

    private static boolean isTechnical;

    private static boolean isCreative;

    private static boolean isTimePeriodTotal;

    private static boolean isSummary;

    public static ProgressBarWindow pbw;

    public static boolean isSummary() {
        return isSummary;
    }

    public static void setSummary(boolean isSummary) {
        MainWindow.isSummary = isSummary;
    }

    public static boolean isTimePeriodTotal() {
        return isTimePeriodTotal;
    }

    public static void setTimePeriodTotal(boolean isTimePeriodTotal) {
        MainWindow.isTimePeriodTotal = isTimePeriodTotal;
    }

    public static boolean isRankings() {
        return isRankings;
    }

    public static boolean isTechnical() {
        return isTechnical;
    }

    public static void setRankings(boolean isRankings) {
        MainWindow.isRankings = isRankings;
    }

    public static void setTechnical(boolean isTechnical) {
        MainWindow.isTechnical = isTechnical;
    }

    public static HttpDownload getSession() {
        return session;
    }

    public static void setSession(HttpDownload session) {
        MainWindow.session = session;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public static boolean isCreative() {
        return isCreative;
    }

    public static void setCreative(boolean isCreative) {
        MainWindow.isCreative = isCreative;
    }

    public MainWindow() {

        pbw = new ProgressBarWindow();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (session != null)
                    session.close();
            }
        });

        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(getClass().getResource("/icon.png")));
        setBounds(200, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Converter");
//        setTitle("MERUGUEZ PUTAIN");

        // getContentPane().setLayout(null);
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        

        addMenu();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        
        createPanelsInternet();
        np.hidePreviousButton();

    }

    public static void main(String[] args) {

        try {
            // set for file chooser look
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        new MainWindow();

    }

    public void showNextPanel() {
        

        getContentPane().remove(panels.get(currentPanel));
        currentPanel++;
        getContentPane().add(panels.get(currentPanel));

        
        if (currentPanel == panels.size()-1)
            np.showValidateButton();
        else
            np.showNextButton();
        
        np.showPreviousButton();
        
        repaint();
        setVisible(true);
    }

    public void showPreviousPanel() {

        getContentPane().remove(panels.get(currentPanel));
        currentPanel--;
        getContentPane().add(panels.get(currentPanel));

        if (currentPanel == 0)
            np.hidePreviousButton();
        else
            np.showPreviousButton();
        np.showNextButton();
        
        repaint();
        setVisible(true);
    }

    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent arg0) {
                if (session != null)
                    session.close();
                System.exit(0);
            }
        });
        mnFile.add(mntmExit);

        JMenu mnSettings = new JMenu("Settings");
        menuBar.add(mnSettings);

       
       

    }

    public static void openFileChooser(final FileType fileType,
            final JTextField field) {
        final JFrame frame = new JFrame(
                "JFileChooser Popup");
        Container contentPane = frame.getContentPane();

        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setControlButtonsAreShown(true);
        contentPane.add(fileChooser, BorderLayout.CENTER);

        fileChooser.setAcceptAllFileFilterUsed(false);

        FileFilter filter = new FileNameExtensionFilter(
                fileType.getDescription(), fileType.getAcceptedExtensions());
        fileChooser.setFileFilter(filter);

        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser theFileChooser = (JFileChooser) actionEvent
                        .getSource();
                String command = actionEvent.getActionCommand();
                if (command.equals(JFileChooser.APPROVE_SELECTION)) {
                    File selectedFile = theFileChooser.getSelectedFile();

                    field.setText(selectedFile.getAbsolutePath());

                    frame.dispose();
                } else if (command.equals(JFileChooser.CANCEL_SELECTION)) {
                    frame.dispose();
                }
            }
        };

        fileChooser.addActionListener(actionListener);

        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void validation() {

        SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {

            @Override
            protected Boolean doInBackground() throws Exception {
                try {
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    doValidationInSwingWorker();
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
                
                return true;

            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        pbw.setVisible(true);
        pbw.setValue(0);
        pbw.setText("");
        try {
            worker.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doValidationInSwingWorker() {

        CampaignChoicePanel ccp = (CampaignChoicePanel)panels.get(1);
        
//        System.out.println(session.getAllRecordsByCampaignID(ccp.getSelectedId()));
//        System.out.println("merguez merguez\n\n\nmerguez\n");
//        System.out.println(FirstSheet.getCountryList(session.getAllRecordsByCampaignID(ccp.getSelectedId())));
        
        ExcelWriter writer = new ExcelWriter();
        
        String fileName =Utils.getFileName(
                ccp.getSelectedCampaignName(),"xls");
        
        writer.writeExcelDocument(fileName,session.getAllRecordsByCampaignID(ccp.getSelectedId()));
        
        pbw.setValue(100);
        
        
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(fileName);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
        
    }


    public void createPanelsInternet() {

        if (panels!=null)
        getContentPane().remove(panels.get(currentPanel));

        panels = new LinkedList<SettingsChoicePanel>();

        panels.add(new LoginPanel(this));
        panels.add(new CampaignChoicePanel());
        

        currentPanel = 0;

        np = new NavigationPanel(this);

        getContentPane().add(np);

        getContentPane().add(panels.get(0));
        repaint();
        revalidate();

    }

    @Override
    public void nextPanel() {
        if (panels.get(currentPanel).isEveryThingOk()) {

            
            showNextPanel();
            

        }
        // else
        // System.out.println(m.getMessages());

       
    }

    @Override
    public void previousPanel() {
        np.showNextButton();

        if (currentPanel > 0) 
            showPreviousPanel();
            
       

        
    }


}
