package neuro.io;

import jblas.matrices.Matrix;

import java.util.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import neuro.adapter.TeachingCase;

public class DataProvider {

    public class TeachingData
            implements Iterator<TeachingCase<Matrix, String>>,
                       Iterable<TeachingCase<Matrix, String>>
    {
        private Iterator<Map.Entry<String, List<TeachingCase<Matrix, String>>>> mapIter_m;
        private Map.Entry<String, List<TeachingCase<Matrix, String>>> entry_m;
        private Iterator<TeachingCase<Matrix, String>> listIter_m;


        public TeachingData() {
            restart();
        }

        public void restart(){
            mapIter_m = loadedData_m.entrySet().iterator();
            proceedToNextGroup();  // like to chember a cartridge
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
            return listIter_m.next ();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }




    private Map<String, List<TeachingCase<Matrix, String>>> loadedData_m;
    private IImageCodec codec_m;
    private FileFilter fileFilter_m;
    private File rootDir_m;
    

    public DataProvider(){
        loadedData_m = Collections.emptyMap();
        codec_m = new BufferedImageCodec();
    }


    public TeachingData provideTeachingData(){
        return new TeachingData();
    }


    public void loadData(int maxGroups){
        loadedData_m = loadData(rootDir_m, maxGroups);
    }


    public FileFilter getFileFilter() {
        return fileFilter_m;
    }
    public void setFileFilter(FileFilter fileFilter) {
        fileFilter_m = fileFilter;
    }


    public File getRootDir() {
        return rootDir_m;
    }
    public void setRootDir(File rootDir) {
        rootDir_m = rootDir;
    }


    public IImageCodec getCodec() {
        return codec_m;
    }
    public void setCodec(IImageCodec codec) {
        codec_m = codec;
    }

    private  Map<String, List<TeachingCase<Matrix, String>>> loadData(File rootDir, int maxGroups){
        Map<String, List<TeachingCase<Matrix, String>>> loadedMap
                = new HashMap<String, List<TeachingCase<Matrix, String>>>(28);

        for(String childName: rootDir.list()){
             File child = new File(rootDir, childName);
             if (child.isDirectory()) continue;
            //TODO Ignore directories begin with '.' .

             assert !loadedMap.containsKey(childName);
             List<TeachingCase<Matrix, String>> valList = loadGroup(child, childName);
             if (valList.isEmpty()) continue;
             loadedMap.put(childName, valList);
            // TODO Throw exception when loadedMap.size() > maxGroups.
             if (loadedMap.size() == maxGroups) break;
        }
        return loadedMap;
    }


    private List<TeachingCase<Matrix, String>> loadGroup(File sourceDir, String keyVal){
        List<TeachingCase<Matrix, String>> loadedList = new ArrayList<TeachingCase<Matrix, String>>(4);
        
        for (String fileName : sourceDir.list()){
            File child = new File(sourceDir, fileName);
            if (!isFileAcceptable(child)) continue;
            try {
                Matrix m = loadMatrix(child);

                TeachingCase<Matrix, String> tcase= new TeachingCase<Matrix, String>( m, keyVal );
                loadedList.add(tcase);
            } catch (IOException e){}
        }

        return loadedList;
    }


    private boolean isFileAcceptable(File file){
        return file.isFile() && ((fileFilter_m == null) || fileFilter_m.accept(file));
    }

    private Matrix loadMatrix(File source) throws IOException {
        //TODO May be change 'img' type to BufferedImageCodec ?
        Object img = codec_m.loadImage(source);
        return codec_m.convert(img);
    }


}
