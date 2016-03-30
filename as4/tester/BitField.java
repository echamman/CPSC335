/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author apu
 */
import java.util.*;
import java.io.*;

public class BitField {
    private final int DEFAULTCAPACITY=16;
    private byte[] data;
    private int nBits, capacity;
    
    public BitField()
    {
        data=new byte[16];
        nBits=0;
        capacity=DEFAULTCAPACITY;
    }
    
    public BitField(LEDataInputStream source, int nDataBits){
        Load(source,nDataBits);
    }
    public int Size(){
        return nBits;
    }
    
    public void Load(LEDataInputStream source, int nDataBits){
        int nBytes=(nDataBits>>3);
        if (nDataBits%8!=0) nBytes++;
        data=new byte[nBytes];
        try{
            source.readFully(data);
            nBits=nDataBits;
            capacity=nBytes;
        }
        catch(IOException e){
            System.out.println("\n\nError reading data...\n\n");
        }
        
    }
    
    public void Save(LEDataOutputStream dst){
        int nBytes=(nBits>>>3)+1;
        try{
            dst.write(data, 0, nBytes);
        }
        catch(IOException e){
            System.out.println("\n\nError writing data...\n\n");
        }
    }

    public int Get_Bit(int pos){
        int bytepos=pos>>>3;
	int bitpos=pos&7;
	return ((data[bytepos]>>>(7-bitpos))&1);
    }
    
    public void Add(byte bits, int bitCount){
        int current=nBits>>>3;
        int bitsRemaining=8-(nBits&7);
        
        if (current+2>=capacity) extend();
        if (bitsRemaining>=bitCount)
            data[current]|=(byte)(((0xFF & (int)bits)>>>(8-bitCount))
                    <<(bitsRemaining-bitCount));
        else
        {
            data[current]|=((0xFF & (int)bits)>>>(8-bitsRemaining));
            data[current+1]=(byte)((bits<<bitsRemaining)&0xFF);
        }
        nBits+=bitCount;
        
    }
    public void Add(byte[] bits, int bitCount){
        int current=nBits>>>3;
        int bitsRemaining=8-(nBits&7);
        
        while (nBits+bitCount+1>=(capacity<<3)) extend();
        
        if (bitsRemaining==8)
            for (int i=0;i<bitCount;i++)
                data[current++]=bits[i];
        else{
            int mask=0xFF;
            int total=(bitCount>>>3);
            
            mask=(mask>>>(8-bitsRemaining));
            for (int i=0;i<total;i++)
            {
                
                data[current++]|=(byte)(((int)bits[i] & 0xFF)>>(8-bitsRemaining));
                data[current]=(byte)(((int)bits[i]<<bitsRemaining)&0xFF);
            }
            nBits+=(total<<3);
            int remainder=bitCount-(total<<3);
            if (remainder!=0) Add(bits[total],remainder);
            return;
        }
        
        nBits+=bitCount;        
    }
    
    private void extend(){
        byte[] tmp=new byte[2*capacity];
        for (int i=0;i<capacity;i++) tmp[i]=data[i];
        data=tmp;
        capacity*=2;
    }
}
