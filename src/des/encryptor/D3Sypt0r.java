package des.encryptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.swing.JOptionPane;
import sun.misc.BASE64Encoder;

public class D3Sypt0r {

    public D3Sypt0r(String str, String filename, int choice, String pass) throws UnsupportedEncodingException {

        String strDataToEncrypt = new String();
        String strCipherText = new String();
        String strDecryptedText = new String();

        try {

            String desKey = pass;
            System.out.println(pass);
            byte[] keyBytes = desKey.getBytes();

            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = factory.generateSecret(new DESKeySpec(keyBytes));
            System.out.println(secretKey);

            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); /* Must specify the mode explicitly as most JCE providers default to ECB mode!! */


            if (choice == 0) {
                desCipher.init(Cipher.ENCRYPT_MODE, secretKey);

                strDataToEncrypt = str;
                byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
                byte[] byteCipherText = desCipher.doFinal(byteDataToEncrypt);
                String strByte = new String(byteCipherText, "UTF-8");
                strCipherText = new BASE64Encoder().encode(byteCipherText);

                System.out.println("Cipher Text is: \n" + strByte);
                writeinFile(byteCipherText, filename);
                JOptionPane.showMessageDialog(null, "File: " + filename + " encrypted successfully.\nSaved to: ENC_" + filename);
            } else if (choice == 1) {
                desCipher.init(Cipher.DECRYPT_MODE, secretKey, desCipher.getParameters());
                byte[] rr = readFile(filename);

                byte[] byteDecryptedText = desCipher.doFinal(rr);
                strDecryptedText = new String(byteDecryptedText);
                System.out.println(" Decrypted Text message is \n" + strDecryptedText);
                writeinFileDec(strDecryptedText, filename);
                JOptionPane.showMessageDialog(null, "File: " + filename + " decrypted successfully.\nSaved to: DEC_" + filename);

            } else {
                System.out.println("ERROR");
            }
        } catch (NoSuchAlgorithmException noSuchAlgo) {
            System.out.println(" No Such Algorithm exists " + noSuchAlgo);
        } catch (NoSuchPaddingException noSuchPad) {
            System.out.println(" No Such Padding exists " + noSuchPad);
        } catch (InvalidKeyException invalidKey) {
            System.out.println(" Invalid Key " + invalidKey);
        } catch (BadPaddingException badPadding) {
            D3Sypt0rFrame.jLabel5.setText("Incorrect Password");
            System.out.println("Incorrect Password");
        } catch (IllegalBlockSizeException illegalBlockSize) {
            System.out.println(" Illegal Block Size " + illegalBlockSize);
        } catch (InvalidAlgorithmParameterException invalidParam) {
            System.out.println(" Invalid Parameter " + invalidParam);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(D3Sypt0r.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeinFile(byte[] bt, String filename) {

        FileOutputStream fos;
        String fname = "ENC_" + filename;
        try {
            fos = new FileOutputStream(fname);
            fos.write(bt);

            fos.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(D3Sypt0r.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(D3Sypt0r.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private byte[] readFile(String filename) {
        String fname = filename;
        File file = new File(fname);
        try {
            FileInputStream fis = new FileInputStream(file);
            byte fileContent[] = new byte[(int) file.length()];
            fis.read(fileContent);
            return fileContent;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(D3Sypt0r.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(D3Sypt0r.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    private void writeinFileDec(String strDecryptedText, String filename) {

        String fname = "DEC_" + filename;
        try {
            PrintWriter pw = new PrintWriter(fname, "UTF-8");
            pw.write(strDecryptedText);
            pw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(D3Sypt0r.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
