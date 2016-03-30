/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author apu
 */
import java.io.*;

public class C335Test {

    public static void Show_Usage(){
        System.out.printf("\n\nInvalid Parameters...\n\n");
        System.out.printf("   USAGE: C335Tester [C335FileName]\n\n");
    }
    
    public static void Process_C335_File(String fname){
        FileInputStream sourceFile;
        try{
            sourceFile=new FileInputStream(fname);
        }
        catch(Exception e){
            System.out.println("Error Opening file: "+fname);
            return;
        }
        LEDataInputStream source=new LEDataInputStream(sourceFile);
        try{
            int fileID=source.readInt();
            if (fileID!=892547907){
                System.out.println("C335 File Signature not found...");
            } 
            System.out.println("\nFILE HEADER:\n");
            System.out.println("   File Signature="+fileID+"(C335)");
            
            short nSymbols=source.readShort();
            int nDataBits=source.readInt();
            System.out.println("   nSymbols="+nSymbols);
            System.out.println("   nDataBits="+nDataBits);
            System.out.println("\nFILE DATA:\n");
            System.out.println("   Symbol Records:\n");
            for (int i=0;i<nSymbols;i++){
                char sym=(char)source.readByte();
                int nBits=source.readUnsignedByte();
                BitField bits=new BitField(source,nBits);
                System.out.println("      Symbol= "+sym);
                System.out.println("      nBits="+nBits);
                System.out.print("      Bits=");
                for (int j=0;j<bits.Size();j++)
                    System.out.print(bits.Get_Bit(j));
                System.out.println("\n");
                
            }
            System.out.println("   Compressed Data:");
            BitField compressedDataBits=new BitField(source,nDataBits);
            for (int i=0;i<compressedDataBits.Size();i++){
                if (i%64==0) System.out.print("\n     ");
                if (i%8==0) System.out.print(" ");
                System.out.print(compressedDataBits.Get_Bit(i));
                
            }
            System.out.println();

        }
        catch(Exception e){
            System.out.println("\n\nError Reading from the file\n\n");
            return;
        }
    }
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length!=1) {Show_Usage();return;}
        
        Process_C335_File(args[0]);
    
    }

}
