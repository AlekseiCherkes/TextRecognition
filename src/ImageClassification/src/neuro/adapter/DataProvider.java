package neuro.adapter;

import jblas.matrices.Matrix;

import java.util.*;

public class DataProvider {

    public class TeachingData
            implements Iterator<TeachingCase<Matrix, String>>,
                       Iterable<TeachingCase<Matrix, String>>
    {
        private Iterator<Map.Entry<String, List<Matrix>>> mapIter_m;
        private Map.Entry<String, List<Matrix>>           entry_m;
        private Iterator<Matrix>                          listIter_m;


        public TeachingData() {
            restart();
        }

        public void restart(){
            mapIter_m = loadedData_m.entrySet().iterator();
            proceedToNextGroup();  // like forsending a projectile
        }

        public boolean proceedToNextGroup(){
            if (!mapIter_m.hasNext()) return false;
            entry_m = mapIter_m.next();
            listIter_m = entry_m.getValue().iterator();
            return true;
        }

        @Override
        public Iterator<TeachingCase<Matrix, String>> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            while(true){
                if (listIter_m == null && !proceedToNextGroup())return false;
                if (listIter_m.hasNext()) return true;
                listIter_m = null;
            }
        }

        @Override
        public TeachingCase<Matrix, String> next() {
            TeachingCase<Matrix, String> pair = new TeachingCase<Matrix, String>();
            pair.setExpectedOutput(entry_m  .getKey());
            pair.setInput         (listIter_m.next ());
            return pair;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }




    private Map<String, List<Matrix>> loadedData_m;

    public TeachingData provideTeachingData(){
        return new TeachingData();
    }


    public void loadData(int maxGroups){
         //TODO: perform loading and translation Image -> Matrix
    }


}
