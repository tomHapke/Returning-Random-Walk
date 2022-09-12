import java.util.function.Function;

public class MatPowerSeries {


    private Function<Integer,Double> coeSequence;

    /**
     * Pass a coefficient sequence as a Function instance.
     */
    public MatPowerSeries(Function<Integer,Double> coeSequence) {
        this.coeSequence=coeSequence;
    }

    /**
     * Compute the series to the k-th term with Matrix A as argument
     */
    public Matrix computeSeries(int k, Matrix A){
        Matrix.setCache(A.getN());
        Matrix result=new Matrix(A.getN());
        Matrix powA=Matrix.getIdentity(A.getN());
        if(k<0){
            System.out.println("invalid k");
            return null;
        }
        if(k==1){
            result.addOR(powA.multiplySkalar(coeSequence.apply(0)));
        }
        for(int j=1;j<=k;j++){
            powA.multiplyOR(A);
            result.addOR(powA.multiplySkalar(coeSequence.apply(j)));
        }
        return result;
    }
}
