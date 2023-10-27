import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {


        BufferedImage loadedImage = null;


        File imageFile = new File("image3.jpg");

        try {
            loadedImage = ImageIO.read(imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // create an array to add the converted colours
        int[][] imageArray = new int[loadedImage.getHeight()][loadedImage.getWidth()];

        for(int i = 0; i < loadedImage.getWidth(); i++){
            for(int j = 0; j < loadedImage.getHeight(); j++){


                Color color = new Color(loadedImage.getRGB(i,j));

                // change colour to grayscale
                int grayScaleRed = (int)(color.getRed() * 0.2989);
                int grayScaleGreen = (int)(color.getGreen() * 0.587);
                int grayScaleBlue = (int)(color.getBlue() * 0.114);



                imageArray[j][i] = grayScaleRed + grayScaleGreen + grayScaleBlue;

            }
        }

        for(int i = 0; i < imageArray.length - 1; i++){
            for(int j = 0; j < imageArray[0].length - 1; j++){

                //int[][] matrix = createMatrix(i,j, loadedImage);
                int[][] matrix = createMatrix(i,j, imageArray);

                int blurredRGB = computeGaussianBlur(matrix);

                // add gray colour to 2d array
                loadedImage.setRGB(j,i,blurredRGB);

                if(i == 4) {
                    System.out.print(blurredRGB + " ");
                }
            }
        }


        renderImage(loadedImage);

    }

    public static int[][] createMatrix(int y, int x, int[][] image){

        int[][] matrix = {
                {0,0,0},{0,0,0},{0,0,0}
        };



        for(int i = 0; i < 3; i++){
            if((y == 0 && i == 0)||(y==image.length && i == 2)){
                continue;
            }
            else {
                for (int j = 0; j < 3; j++) {
                    if((x == 0 && j == 0)||(x == image[0].length && j == 2)){
                        continue;
                    }
                    else {
                        matrix[i][j] = image[y + i - 1][x + j - 1];
                    }
                }
            }
        }

        return matrix;
    }
    public static int computeGaussianBlur(int[][] matrix){
        float sum = 0;
        int [][] kernel =
                {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++) {
                sum +=  (float)matrix[i][j] * ((float)kernel[i][j])/16;
            }
        }

        int ans = (int)sum;

        return ans;
    }

    //You can call this method to display your image on your screen
    //Make sure you are not calling this method when you run the test cases
    //A1 in Codio has an xserver: click on Virtual Desktop on the top bar (this should work)
    //Adapted from: https://stackoverflow.com/questions/1626735/how-can-i-display-a-bufferedimage-in-a-jframe
    public static void renderImage(BufferedImage image){
        JFrame frame;
        JLabel label;
        frame=new JFrame();
        frame.setSize(image.getWidth(), image.getHeight());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        label=new JLabel();
        label.setIcon(new ImageIcon(image));

        frame.getContentPane().add(label, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}

/*References
 * https://stackoverflow.com/questions/7427141/how-to-get-rgb-value-from-hexadecimal-color-code-in-java
 *
 */