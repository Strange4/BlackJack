import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileWriter;


public class Files
{

    // Finds a user from the files and returns all of his data
    public static String findUser(String filePath, String userAndPassword) throws IOException
    {
        File file = new File(filePath); 
        BufferedReader br = new BufferedReader(new FileReader(file)); 
        String st;

        while ((st = br.readLine()) != null){
            Pattern pattern = Pattern.compile("^"+userAndPassword+" ", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(st);
    
            boolean foundMatch = matcher.find();
            if(foundMatch){
                br.close();
                return st;
            }
        }
        br.close();
        return null;
    }

    // appends to an existing file
    public static void writeLine(String filePath, String input)
    {
        File file = new File(filePath);
        if(!file.exists())
            throw new IllegalArgumentException("This file doesn't exist");

        
        try{
            FileWriter writer = new FileWriter(file, true);
            writer.write(input+"\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Something bad happend. Try googling it");
            e.printStackTrace();
        }
    }

    // Updates the money for a user
    // Returns true if the money from a user was updated
    public static boolean updateMoney(String filePath, String userAndPassword, String money)
    {
        int userDataLocation =-1;
        try {
            userDataLocation = findLine(filePath, userAndPassword+" ");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(userDataLocation == -1)
            return false;

        int moneyInAccount;
        try{
            moneyInAccount = Integer.parseInt(money);
        } catch (Exception e){
            return false;
        }
        deleteLine(filePath, userDataLocation);

        writeLine(filePath, userAndPassword+" "+moneyInAccount);
        return true;
    }

    // Deletes a line from a file
    public static void deleteLine(String filePath, int lineToDelete)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            
            //String buffer stores the contents of the file
            StringBuffer sb = new StringBuffer("");
            
            int lineCounter=1;
            String line;
            while((line = br.readLine()) != null)
            {
                // Store in the string buffer
                if(lineCounter != lineToDelete)
                {
                    sb.append(line+"\n");
                }
                lineCounter++;
            }
            br.close();

            FileWriter fw = new FileWriter(new File(filePath));

            //Write the stored lines to the the file
            fw.write(sb.toString());;
            fw.close();
        } 
        catch (Exception e)
        {
            System.out.println("An exception was thrown: "+e.getMessage());
        }
    }

    // Finds a line number from a file using a string toFind
    private static int findLine(String filePath, String toFind) throws IOException
    {
        File file = new File(filePath); 
        BufferedReader br = new BufferedReader(new FileReader(file)); 
        String st;
    
        int lineCounter = 1;
        while ((st = br.readLine()) != null){
            Pattern pattern = Pattern.compile("^"+toFind, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(st);
            
            boolean foundMatch = matcher.find();
            
            if(foundMatch){
                br.close();
                return lineCounter;
            }
            lineCounter++;
        }
        br.close();
        return -1;
    }

    // creates a txt file inside a folder. Returns the filepath
    public static String createFile(String folderName, String fileName)
    {
        File folder = new File(folderName);
        if(!folder.exists())
        {
            if(folder.mkdirs())
            System.out.println("The folder has been created");
        }
        
        String filePath = folder.getPath()+ "/" + fileName + ".txt";
        File file = new File(filePath);
        try{
            if(file.createNewFile())
            {
                System.out.println("File created: "+file.getName());
            }
        } catch (IOException e) {
            System.out.println("Something bad happend. Try googling it");
            e.printStackTrace();
        }
        return file.getPath();
    }

    // THE REST OF THE METHODS ARE FOR TESTING ONLY

    //Deletes the content of a file
    public static void clearFile(String filePath)
    {
        File file = new File(filePath);
        if(!file.exists())
            throw new IllegalArgumentException("This file doesn't exist");
        
        try{
            FileWriter writer = new FileWriter(filePath);
            writer.write("");
            writer.close();
            System.out.println("Successfully cleared the file");
        } catch (IOException e) {
            System.out.println("Something bad happend. Try googling it");
            e.printStackTrace();
        }
    }

    // Prints useful information about the file
    public static void FileInfo(String filePath)
    {
        File myObj = new File(filePath);
        if (myObj.exists()) {
            System.out.println("File name: " + myObj.getName());
            System.out.println("Absolute path: " + myObj.getAbsolutePath());
            System.out.println("Writeable: " + myObj.canWrite());
            System.out.println("Readable " + myObj.canRead());
            System.out.println("File size in bytes " + myObj.length());
        } else {
            System.out.println("The file does not exist.");
        }
    }

    // prints the contents of a file
    public static void printFile(String filePath) throws IOException
    {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        
        String st;
        while ((st = br.readLine()) != null)
        {
            System.out.println(st);
        }
        br.close();
    }

    // deletes a file
    public static void deleteFile(String filePath)
    {
        File file = new File(filePath);
        if(file.delete())
            System.out.println("File "+file.getName()+" deleted");
        else
            System.out.println("File "+file.getName()+" could not be deleted");
    }
}