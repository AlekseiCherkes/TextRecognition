package neuro.adapter;


public class TeachingCase<TIn, TOut> {
    private TIn  input_m;
    private TOut expectedOutput_m;

    public TeachingCase() { }

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
}
