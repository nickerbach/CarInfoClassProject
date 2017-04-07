import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;


public class EdmundsAPI {

    public static void main(String argv[]) {
        //getting car info from the user
        int year = getYear();
        String make = getMake(), model = getModel();

        //creating API url
        String fullUrl = getApiUrl(year, make, model);

        try {
            //make web request and print results
            getResults(fullUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static void getResults(String fullUrl) throws IOException {
        URL url = new URL(fullUrl);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String strTemp = "";
        while (null != (strTemp = br.readLine())) {

            if (strTemp.contains("{\"styles\":[],\"stylesCount\":0}")) {
                System.out.println("No results found.");
            } else {
                System.out.println(strTemp);
            }
        }
    }

    private static String getApiUrl(int year, String make, String model) {
        String baseUrl = "https://api.edmunds.com/api/", method = "vehicle/v2/",
                queryParams = make + "/" + model + "/" + year + "/styles?view=full&fmt=json", apiKey = "&api_key=dg5rj2zrwv3rjy6udqygv7ze";
        String fullUrl = baseUrl + method + queryParams + apiKey;

        System.out.println("Search URL: " + fullUrl);
        return fullUrl;
    }

    private static String getModel() {
        Scanner reader3 = new Scanner(System.in);
        System.out.print("Enter a model: ");
        return reader3.nextLine();
    }

    private static String getMake() {
        Scanner reader2 = new Scanner(System.in);
        System.out.print("Enter a make: ");
        return reader2.nextLine();
    }

    private static int getYear() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter a year: ");
        return reader.nextInt();
    }

}
