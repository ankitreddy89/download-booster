import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
            JSONParser parser = new JSONParser();
            Helpers helpers = new Helpers();

            //Parsing the json config file
            Object obj = parser.parse(
                    new FileReader(
                            "../../../config.json"));
            JSONObject jsonObject =  (JSONObject) obj;
            int numberOfChunks = helpers.convertToBytes((String) jsonObject.get("numberOfChunks"));
            int chunkSize = helpers.convertToBytes((String) jsonObject.get("chunkSize"));
            int totalFileSize = helpers.convertToBytes((String) jsonObject.get("totalFileSize"));

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
                    String sourceUrl = args[0];
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

        } catch (IOException | ParseException exception) {
            exception.printStackTrace();
        }
    }
}
