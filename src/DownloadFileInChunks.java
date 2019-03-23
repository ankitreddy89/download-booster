import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;
/**
 * The DownloadFileInChunks program implements an application that
 * downloads files from a source in defined chunks.
 *
 * @author  Ankit Reddy Pati
 * @since   2019-03-23
 */
public class DownloadFileInChunks {

    public static void main(String[] args){
        try {
            Helpers helpers = new Helpers();
            Properties properties = new Properties();
            InputStream input = DownloadFileInChunks.class.getClassLoader().getResourceAsStream(
                    "config.properties");
            properties.load(input);

            int numberOfChunks = Integer.parseInt(properties.getProperty("numberOfChunks"));
            int chunkSize = helpers.convertToBytes(properties.getProperty("chunkSize"));
            int totalFileSize = helpers.convertToBytes(properties.getProperty("totalFileSize"));

            //Source url provided by the user
            String sourceUrl;
            try {
                sourceUrl = args[0];
            }
            catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Source URL not provided!");
                System.out.print("Enter the source URL: ");
                Scanner scanner = new Scanner(System.in);
                sourceUrl = scanner.next();
            }

            byte[] bytes = new byte[chunkSize];
            int start = 0;
            for (int a = 0; a < numberOfChunks; a++) {
                int end;
                if (start < totalFileSize) {
                    if ((start + chunkSize) > totalFileSize) {
                        end = totalFileSize;
                    } else {
                        end = chunkSize * (a + 1);
                    }
                    //Creating a connection to the source
                    URL url = new URL(sourceUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Range", "bytes=" + start + "-" + end);
                    urlConnection.connect();

                    System.out.println("Chunk: " + (a + 1));
                    System.out.println("Request Method: " + urlConnection.getRequestMethod());
                    System.out.println("Response Code: " + urlConnection.getResponseCode());
                    System.out.println("Content-Length: " + urlConnection.getContentLengthLong());
                    System.out.println("Range: " + urlConnection.getRequestProperty("Range"));

                    //Reading content
                    BufferedInputStream is = new BufferedInputStream(urlConnection.getInputStream());
                    is.readNBytes(bytes, 0, end-start);

                    String destFile = "chunk_" + (a + 1) + ".jar";;
                    if (args.length == 2){
                        destFile = args[1] + "\\" + destFile;
                    }
                    //Writing to the file
                    FileOutputStream stream = new FileOutputStream(destFile);
                    stream.write(bytes, 0, end-start);
                    start += chunkSize;
                    System.out.println("------------------------------");
                }
                else{
                    System.out.println("No more data ," + totalFileSize + " bytes retrieved, terminating.");
                    break;
                }
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
