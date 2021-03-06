package neuro.io;

import jblas.matrices.Matrix;
import neuro.adapter.TeachingCase;

import java.io.*;
import java.util.*;

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
    private int files_count;



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

    public List<String> getTypes(){
        //TODO : it is VERY ugly!
        return new ArrayList<String>(loadedData_m.keySet());

    }

    public int getDataCount(){
        return files_count;
    }

    private  Map<String, List<TeachingCase<Matrix, String>>> loadData(File rootDir, int maxGroups){
        Map<String, List<TeachingCase<Matrix, String>>> loadedMap
                = new HashMap<String, List<TeachingCase<Matrix, String>>>(28);

        for(String childName: rootDir.list()){
             File child = new File(rootDir, childName);
             if (!isDirectoryAcceptable(child)) continue;

             assert !loadedMap.containsKey(childName);
             List<TeachingCase<Matrix, String>> valList = loadGroup(child, childName);
             if (valList.isEmpty()) continue;
             loadedMap.put(childName, valList);
            
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
                tcase.setCaseName(child.getName());
                loadedList.add(tcase);
                files_count++;
            } catch (IOException e){}
        }

        return loadedList;
    }


    private boolean isDirectoryAcceptable(File file){
        return file.isDirectory() && ((fileFilter_m == null) || fileFilter_m.accept(file));
    }


    private boolean isFileAcceptable(File file){
        return file.isFile() && ((fileFilter_m == null) || fileFilter_m.accept(file));
    }

    private Matrix loadMatrix(File source) throws IOException {
        //TODO No, for there is QImage also.
        Object img = codec_m.loadImage(source);
        return codec_m.convert(img);
    }




}
