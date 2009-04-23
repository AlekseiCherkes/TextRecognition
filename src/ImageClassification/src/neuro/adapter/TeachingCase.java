package neuro.adapter;


public class TeachingCase<TIn, TOut> {
    private TIn  input_m;
    private String caseName_m;
    private TOut expectedOutput_m;

    public TeachingCase( TIn input, TOut expectedOutput ){
        input_m = input;
        expectedOutput_m = expectedOutput; 
    }

    public String getCaseName() {
        return caseName_m;
    }

    public void setCaseName(String caseName) {
        caseName_m = caseName;
    }

    public TIn getInput() {
        return input_m;
    }

    public void setInput(TIn input) {
        input_m = input;
    }

    public TOut getExpectedOutput() {
        return expectedOutput_m;
    }

    public void setExpectedOutput(TOut expectedOutput) {
        expectedOutput_m = expectedOutput;
    }

    @Override
    public String toString() {
        return "TeachingCase{"
             + "caseName='" + caseName_m
             + ", expectedOutput=" + expectedOutput_m
             + '}';
    }
}
