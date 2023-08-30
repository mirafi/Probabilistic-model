package mi.stat.model.entropy.core;



import java.util.logging.Logger;

public class Result {
    int positiveValue;
    int negativeValue;
    Logger logger = Logger.getLogger(String.valueOf(Result.class));

    public int getPositiveValue() {
        return positiveValue;
    }

    public void setPositiveValue(int positiveValue) {
        this.positiveValue = positiveValue;
    }

    public int getNegativeValue() {
        return negativeValue;
    }
    public double getPositiveValueRatio(){
        return ( (double) this.positiveValue/(double) ( this.negativeValue + this.positiveValue )  );
    }
    public double getNegativeValueRatio(){
        return ( (double) this.negativeValue/(double) ( this.negativeValue + this.positiveValue )  );
    }

    public double getTotal(){
        return  this.negativeValue  + this.positiveValue ;
    }

    public void setNegativeValue(int negativeValue) {
        this.negativeValue = negativeValue;
    }
    public void    increasePositiveValue(){
        this.positiveValue++;
    }

    public void increaseNegativeValue(){
        this.negativeValue++;
    }

    /**
     * Note : positiveValue and negativeValue both can not be 0
     * */
    public boolean doesItGiveOnlyOneResult(){
        return (  this.hasOnlyPositiveResult() ) || ( this.hasOnlyNegativeResult() );
    }

    public boolean hasOnlyPositiveResult(){
        return ( this.negativeValue == 0 && this.positiveValue > 0 ) ;
    }

    public boolean hasOnlyNegativeResult(){
        return  ( this.positiveValue == 0 && this.negativeValue >0 );
    }

    /**
     * 0 = Not give only one result
     * 1 = Positive result
     * 0 = Negative result
     * */
    public int getResultType(){
        return hasOnlyNegativeResult() ? -1: ( hasOnlyPositiveResult() ? 1 :0 ) ;
    }

    public void setVale(Result r){
        this.positiveValue = r.positiveValue;
        this.negativeValue = r.negativeValue;
    }

    public void increaseValue(String positiveResultValue,String negativeResult,String  resultValue){
        if(positiveResultValue.equals(resultValue)){
            this.increasePositiveValue();
        }else if (negativeResult.equals(resultValue)){
            this.increaseNegativeValue();
        }else{

            logger.info("Value not did not update for positiveValue "
                                +positiveValue+" negativeValue "
                                +negativeValue+" resultValue "+resultValue);
        }
    }

    @Override
    public String toString() {
        return "["+ positiveValue +", " + negativeValue + "]";
    }
}

