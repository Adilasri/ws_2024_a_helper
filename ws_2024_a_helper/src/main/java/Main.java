import com.sun.jdi.Value;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.function.Function;
import java.net.URI;


public class Main {
    public static void main(String[] args) {
        List<List<String>> lines = readFile();


        //1. מהי שנת הלידה הממוצעת של נשים? (יש לשלוח מספר שלם כתשובה - לעגל כלפי מטה)
        int cnt1 = 0;
        int sum1 = 0;
        int average1;
        String female = "Female";
        for (List<String> l: lines){
            if (female.equals(l.get(5))){
                sum1 += Integer.valueOf(l.get(4));
                cnt1 ++;
            }

        }
        average1 = sum1/cnt1;
        System.out.println(average1);




        //2. מהו השם הפרטי הנפוץ ביותר אצל גברים?
        Map<String, Integer> name = new HashMap<>();
        String male = "Male";

        for (List<String> l: lines) {
            String maleName = l.get(2);
            if (male.equals(l.get(5))){
            if (name.containsKey(maleName)) {
                name.put(maleName,name.get(maleName) + 1);
            }else {
                name.put(maleName, 0);
            }
        }
        }

        Map<String, Integer> sortedMap = name.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // במקרה של ערכים זהים, המפתח הראשון נשמר
                        LinkedHashMap::new // שמירת סדר הכניסות הממוינות במפה חדשה
                ));

        // הדפסת המפה הממוינת
        sortedMap.forEach((key, value) -> System.out.println(key + ": " + value));



       // 3. מהו ממוצע החשבונית עבור לקוחות שיש להם בשם הפרטי a או A?

        int cnt3 = 0;
        int sum3 = 0;
        int averg3 = 0;

        for (List<String> l: lines) {
            if (l.get(2).contains("A") || l.get(2).contains("a") ) {
                sum3 += Integer.valueOf(l.get(7));
                cnt3++;
                //רצוי בשלב הזה להדפיס את הרשימה לראות אם מתקיים התנאי.
            }
        }


        averg3 = sum3/cnt3;
        System.out.println(averg3);




        //4. כמה לקוחות מעיר שמתחילה במילה Lake שילמו יותר מ-400
        int cnt4=0;

        for(List<String> l: lines){
            if (l.get(6).contains("Lake") && Integer.valueOf(l.get(7)) > 400){
                cnt4++;

            }
        }
        System.out.println(cnt4);



       //  5. לקוחות של איזו עיר רכשו בסכום הכסף הכולל הגדול ביותר?
        Map<String, Integer> city = new HashMap<>();
        for (List<String> l: lines) {
            String cityName = l.get(6);
            int cityPrice = Integer.valueOf(l.get(7));
                if (city.containsKey(cityName)) {
                    city.put(cityName, city.get(cityName) + cityPrice);
                }else {
                    city.put(cityName, cityPrice);
                }

        }

        Map<String, Integer> sortedCity = city.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // במקרה של מפתחות זהים, המפתח הראשון נשמר
                        LinkedHashMap::new // שמירת סדר המיון במפה חדשה
                ));

        // הדפסת המפה הממוינת
        sortedCity.forEach((key, value) -> System.out.println(key + ": " + value));





       // 6. מהו מספר תעודת הזהות שמופיע יותר מפעם אחת ברשימה
        Map<String, Integer> id = new HashMap<>();
        for (List<String> l: lines) {
            String idNum = l.get(1);
            if (city.containsKey(idNum)) {
                System.out.println(idNum);
            }else {
                city.put(idNum, 1);
            }

        }



        //הקוד של התמונה

        try {
            // טוענים את התמונה מקובץ
            BufferedImage image = ImageIO.read(new File("C:\\Users\\Owner\\IdeaProjects\\ws_2024_a_helper\\images\\32.png"));

            // מפה לשמירת ספירת הפיקסלים לכל צבע
            Map<Integer, Integer> colorCountMap = new HashMap<>();

            // עוברים על כל הפיקסלים בתמונה
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    int pixelColor = image.getRGB(x, y);
                    Color color = new Color(pixelColor);

                    // מתעלמים מלבן (RGB: 255, 255, 255)
                    if (color.equals(Color.WHITE)) continue;

                    colorCountMap.put(pixelColor, colorCountMap.getOrDefault(pixelColor, 0) + 1);
                }
            }

            // מיון הצבעים לפי כמות הפעמים שהם מופיעים (בסדר יורד)
            List<Map.Entry<Integer, Integer>> sortedColors = colorCountMap.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            // בדיקת אם יש לפחות 3 צבעים שונים
            if (sortedColors.size() < 3) {
                System.out.println("אין מספיק צבעים שונים בתמונה.");
            } else {
                // קבלת הצבע השלישי בדומיננטיות
                Color thirdDominantColor = new Color(sortedColors.get(2).getKey());
                System.out.println("הצבע השלישי הכי דומיננטי: " + thirdDominantColor);
                System.out.println("RGB: " + thirdDominantColor.getRed() + ", " +
                        thirdDominantColor.getGreen() + ", " + thirdDominantColor.getBlue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        // זימון בקשות API
        String megic = "2GNA56H";
        apiPost1(megic);// שליחת בקשת API לשאלה 1
        apiPost2(megic);//שליחת בקשת API לשאלה עם התמונה


    }
    public static void apiPost1(String megic) {

        try {
            CloseableHttpClient client = HttpClients.createDefault();

            URI uri = new URIBuilder("https://app.seker.live/fm1/answer-file")
                    .setParameter("magic", megic)
                    .addParameter("question", "1")
                    .addParameter("answer","1981" )
                    .build();
            HttpPost request = new HttpPost(uri);
            CloseableHttpResponse response = client.execute(request);
            String output = EntityUtils.toString(response.getEntity());
            System.out.println(output);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public static void apiPost2(String megic) {

        try {
            CloseableHttpClient client = HttpClients.createDefault();

            URI uri = new URIBuilder("https://app.seker.live/fm1/answer-image")
                    .setParameter("magic", megic)
                    .addParameter("red", "135")
                    .addParameter("green","1" )
                    .addParameter("blue","214" )
                    .build();
            HttpPost request = new HttpPost(uri);
            CloseableHttpResponse response = client.execute(request);
            String output = EntityUtils.toString(response.getEntity());
            System.out.println(output);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }







    public static List<List<String>> readFile () {
        List<List<String>> lines = new ArrayList<>();
        try {
            File file = new File("data.csv");
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] tokens = line.split(",");
                    List<String> tokensList = new ArrayList<>(Arrays.asList(tokens));
                    lines.add(tokensList);
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }


}
