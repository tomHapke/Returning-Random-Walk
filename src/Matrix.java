import java.io.*;
import java.math.BigDecimal;
import java.util.Locale;

public class Matrix {

    /**
     * Two class variables of Matrix Type,that act as cache when performing matrix computations.
     */
    public static Matrix cache1;
    public static Matrix cache2;

    /**
     * 2d-array with dimensions (n,n)
     */
    private double[][] a;
    private int n;

    public static void setCache(int n){
        cache1= new Matrix(n);
        cache2= new Matrix(n);
    }


    public Matrix(int n){
        this.n=n;
        a= new double[n][n];
    }

    /**
     * Returns the nxn-identity matrix
     */
    public static Matrix getIdentity(int n){
        Matrix i = new Matrix(n);
        for(int j=0;j<n;j++){
            i.setA(j,j,1.0);
        }
        return i;
    }

    /**
     * Reads a txt-file and returns a matrix read out of the folder where this class is located.
     * Readable txt-files must suffice the format explained in README.md
     */
    public static Matrix readMatrix(String filename){

        try{
            FileReader file= new FileReader(filename);
            BufferedReader buff= new BufferedReader(file);
            String s = buff.readLine();

            int r=0;

            while (r<s.length()){
                if(isDigit(s.charAt(r))){
                    r++;
                }else {
                    break;
                }
            }

            int n= Integer.parseInt(s.substring(0,r));

            Matrix m = new Matrix(n);
            int i=0;
            int j=0;
            StringBuilder num;
            double value;
            while(buff.ready()){
                s=buff.readLine();
                for(int k=0;k<s.length();k++){
                    if(isDigit(s.charAt(k))){
                        num =new StringBuilder();
                        while (isDigit(s.charAt(k))){
                            num.append(s.charAt(k));
                            k++;
                        }
                        value= Double.parseDouble(num.toString());
                        m.setA(i,j,value);
                        k--;
                        j++;
                    }
                    else if(k>0&&s.charAt(k)=='|'){
                        break;
                    }
                }
                j=0;
                i++;
            }
            buff.close();
            return m;

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.out.println("Fehler beim Einlesen der Matrix");
        return null;
    }

    private static boolean isDigit(char c){
        char[] digits=new char[]{'.','1','2','3','4','5','6','7','8','9','0'};
        for(int i=0;i<digits.length;i++){
            if(c==digits[i]){
                return true;
            }
        }
        return false;
    }

    /**
     * Multiplies the Matrix-instance with the passed matrix m,
     * BUT returns a new instance as a result.
     */
    public Matrix multiply(Matrix m){
        Matrix r=new Matrix(n);
        double s=0.0;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                for (int k=0;k<n;k++){
                    s+=this.getA(i,k)*m.getA(k,j);
                }
                r.setA(i,j,s);
                s=0.0;
            }
        }
        return r;
    }

    /**
     * Multiplies the Matrix-instance with the passed matrix m and
     * overrides the instance.
     */
    public void multiplyOR(Matrix m) throws IllegalArgumentException{
        if(cache1!=null&&cache1.getN()==n){
            double s=0.0;
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    for (int k=0;k<n;k++){
                        s+=this.getA(i,k)*m.getA(k,j);
                    }
                    cache1.setA(i,j,s);
                    s=0.0;
                }
            }

            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    this.setA(i,j,cache1.getA(i,j));
                }
            }
        }
        else {
            throw new IllegalArgumentException("Cache 1 not  (correctly)  initialized!");
        }
    }

    /**
     * Multiplies the Matrix-instance with the passed sklalar l,
     * BUT returns a new instance as a result.
     */
    public Matrix multiplySkalar(double l){
        Matrix r=new Matrix(n);
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                r.setA(i,j,l*this.getA(i,j));
            }
        }
        return r;
    }

    /**
     * Multiplies the Matrix-instance with the passed sklalar l and
     * overrides the instance.
     */
    public void multiplySkalarOR(double l){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                this.setA(i,j,l*this.getA(i,j));
            }
        }
    }

    /**
     * Adds the Matrix-instance and the passed matrix m together,
     * BUT returns a new instance as a result.
     */
    public Matrix add(Matrix m){
        Matrix r=new Matrix(n);
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                r.setA(i,j,this.getA(i,j)+m.getA(i,j));
            }
        }
        return r;
    }

    /**
     * Adds the Matrix-instance and the passed matrix m together and
     * overrides the instance.
     */
    public void addOR(Matrix m){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                this.setA(i,j,this.getA(i,j)+m.getA(i,j));
            }
        }
    }

    /**
     * Subtracts the passed matrix m from the Matrix-instance,
     * BUT returns a new instance as a result.
     */
    public Matrix sub(Matrix m){
        Matrix r=new Matrix(n);
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                r.setA(i,j,this.getA(i,j)-m.getA(i,j));
            }
        }
        return r;
    }

    /**
     * Subtracts the passed matrix m from the Matrix-instance and
     * overrides the instance.
     */
    public void subOR(Matrix m){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                this.setA(i,j,this.getA(i,j)-m.getA(i,j));
            }
        }
    }

    /**
     * Takes the Matrix-instance to the k-th power,
     * BUT returns a new instance as a result.
     */
    public Matrix pow(int k) throws IllegalArgumentException{
        if(k<0){
            throw new IllegalArgumentException("no negative Exponent alllowed here");
        }
        if(k==0){
            return Matrix.getIdentity(n);
        }
        Matrix temp = this;
        for(int i=1;i<k;k++){
            temp=temp.multiply(this);
        }
        return temp;
    }

    /**
     * Takes the Matrix-instance to the k-th power and
     * overrides the instance.
     */
    public void powOR(int k) throws IllegalArgumentException{
        if(cache2==null||cache2.getN()!=n){
            throw new IllegalArgumentException("Cache 2 not (correctly) initialized!");
        }

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                cache2.setA(i,j,this.getA(i,j));
            }
        }

        if(k<0){
            throw new IllegalArgumentException("no negative Exponent alllowed here");
        }
        if(k==0){
            for(int i=0;i<n;i++){
                for(int j=0;j<n;j++){
                    if(i==j){
                        this.setA(i,j,1.0);
                    }
                    else {
                        this.setA(i,j,0.0);
                    }
                }
            }
            return;
        }
        for(int i=1;i<k;i++){
            System.out.println(i+"-th Power calculated");
            this.multiplyOR(cache2);
        }
    }

    /**
     * Getter And Setter
     */
    public double[][] getA() {
        return a;
    }

    public int getN() {
        return n;
    }

    public double getA(int i, int j) {
        return a[i][j];
    }

    public void setA(int i,int j, double value) {
        this.a[i][j]=value;
    }

    /**
     * Deep Copy of a Matrix-instance
     */
    public Matrix copy(){
        Matrix m= new Matrix(n);
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                m.setA(i,j,this.getA(i,j));
            }
        }
        return m;
    }


    @Override
    public String toString(){
        StringBuilder s= new StringBuilder();
        for(int i=0;i<n;i++){
            s.append("|");
            for(int j=0;j<n-1;j++){
                s.append(this.getA(i,j));
                s.append(" , ");
            }
            s.append(this.getA(i,n-1));
            s.append("|");
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Writes the Matrix-instance in a txt-file according to the format explained in README.md
     */
    public void writeMatrix(String filename) throws IOException {
        try{
            Writer  file= new PrintWriter(filename);
            BufferedWriter  data= new BufferedWriter (file);
            data.write(Integer.toString(n));
            data.write(System.lineSeparator());
            for(int i=0;i<n;i++){
                data.write('|');
                for(int j=0; j<n-1;j++){
                    data.write(Double.toString(this.getA(i,j)));
                    data.write(" , ");
                }
                data.write(Double.toString(this.getA(i,n-1)));
                data.write('|');
                data.write(System.lineSeparator());
            }
            data.close();
        }
        catch (FileNotFoundException f){
            f.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Matrix m= Matrix.readMatrix("A.txt");
        m.writeMatrix("C.txt");
        System.out.println(m);
    }
}
