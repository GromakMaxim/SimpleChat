package org.example.client_application;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class ClientTest {
    private final String path = "src/main/java/org/example/client_application/settings/client_settings.txt";
    Client client = new Client();
    public ClientTest() throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
    }

    //    @Test
//    public void test_settingsFileIsExist(){
//        File settingsFile = new File("src/main/java/org/example/client_application/settings/client_settings.txt");
//        Assert.assertTrue(settingsFile.exists());
//    }
    @Test
    public void test_settingsFileIsExistWithWrongPath() {
        File settingsFile = new File("src/main/java/org/example/client_application/settings/_settings.txt");
        Assert.assertFalse(settingsFile.exists());
    }

    @Test
    public void test_createDefaultSettingsFileIfNotExist() throws FileNotFoundException, UnsupportedEncodingException {
        client.createDefaultSettingsFileIfNotExist(path);
        File settingsFile = new File(path);
        Assert.assertTrue(settingsFile.exists());
    }

    @Test
    public void test_getServerPortParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String actual = client.getServerPortParameterFromSettingsFile();
        Assert.assertNotEquals(0, Integer.parseInt(actual));
        Assert.assertNotEquals("", actual);
        Assert.assertNotEquals(null, actual);
    }

    @Test
    public void test_getServerHostParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        String expected = "localhost";
        String actual = client.getServerHostParameterFromSettingsFile();
        System.out.println("Actual:" + actual + "Expected: " + expected);
        Assert.assertEquals(expected, actual);
        Assert.assertNotEquals(null, actual);
        Assert.assertNotEquals("", actual);
    }

    @Test
    public void test_getWindowHeightParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        int actual = client.getWindowHeightParameterFromSettingsFile();
        Assert.assertNotEquals(0, actual);
    }

    @Test
    public void test_getWindowWidthParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        int actual = client.getWindowWidthParameterFromSettingsFile();
        Assert.assertNotEquals(0, actual);
    }

    //
    @Test
    public void test_getWindowYParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        int actual = client.getWindowYParameterFromSettingsFile();
        Assert.assertNotEquals(0, actual);
    }

    @Test
    public void test_getWindowXParameterFromSettingsFile() throws FileNotFoundException, UnsupportedEncodingException {
        int actual = client.getWindowXParameterFromSettingsFile();
        Assert.assertNotEquals(0, actual);
    }

    @Test
    public void test_readSettingsFileAndGetParameter() {
        String parameter1 = "server_port";
        String actual1 = client.readSettingsFileAndGetParameter(parameter1);
        Assert.assertNotEquals(0, Integer.parseInt(actual1));
        Assert.assertNotEquals("", actual1);
        Assert.assertNotEquals(null, actual1);

        String parameter2 = "server_host";
        String actual2 = client.readSettingsFileAndGetParameter(parameter2);
        String expected = "localhost";
        Assert.assertEquals(expected, actual2);
        Assert.assertNotEquals(null, actual2);
        Assert.assertNotEquals("", actual2);

        String parameter3 = "window_x";
        int actual3 = Integer.parseInt(client.readSettingsFileAndGetParameter(parameter3));
        Assert.assertNotEquals(0, actual3);

        String parameter4 = "window_y";
        int actual4 = Integer.parseInt(client.readSettingsFileAndGetParameter(parameter4));
        Assert.assertNotEquals(0, actual4);

        String parameter5 = "window_width";
        int actual5 = Integer.parseInt(client.readSettingsFileAndGetParameter(parameter5));
        Assert.assertNotEquals(0, actual5);

        String parameter6 = "window_height";
        int actual6 = Integer.parseInt(client.readSettingsFileAndGetParameter(parameter6));
        Assert.assertNotEquals(0, actual6);

    }
}