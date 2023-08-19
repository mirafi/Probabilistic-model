package mi.stat.model.entropy.core;


public class Result {
    int positiveValue;
    int negativeValue;

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
    public void increasePositiveValue(){
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

    public String getResultName(String positiveResultName,String negativeResultName){
        switch (this.getResultType()){
            case 1  : return positiveResultName ;
            case -1  : return negativeResultName ;
            default: throw new RuntimeException();
        }
    }

    @Override
    public String toString() {
        return "["+ positiveValue +", " + negativeValue + "]";
    }
}

