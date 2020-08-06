package com.prolog.eis.util.resultAnnotationBuild;


import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;


public class ResultAnnotationBuilder  implements PackageScanner{
    private String basePackage;
    private ClassLoader cl;

    /**
     * 初始化
     * @param basePackage
     */
    public ResultAnnotationBuilder(String basePackage) {
        this.basePackage = basePackage;
        this.cl = getClass().getClassLoader();
    }
    public ResultAnnotationBuilder(String basePackage, ClassLoader cl) {
        this.basePackage = basePackage;
        this.cl = cl;
    }
    /**
     *获取指定包下的所有字节码文件的全类名
     */
    public List<String> getFullyQualifiedClassNameList() throws IOException {

        return doScan(basePackage, new ArrayList<String>());
    }

    /**
     *doScan函数
     * @param basePackage
     * @param nameList
     * @return
     * @throws IOException
     */
    private List<String> doScan(String basePackage, List<String> nameList) throws IOException {
        String splashPath = StringUtil.dotToSplash(basePackage);
        URL url = cl.getResource(splashPath);   //file:/D:/WorkSpace/java/ScanTest/target/classes/com/scan
        String filePath = StringUtil.getRootPath(url);
        List<String> names = null; // contains the name of the class file. e.g., Apple.class will be stored as "Apple"
        if (isJarFile(filePath)) {// 先判断是否是jar包，如果是jar包，通过JarInputStream产生的JarEntity去递归查询所有类

            names = readFromJarFile(filePath, splashPath);
        } else {

            names = readFromDirectory(filePath);
        }
        for (String name : names) {
            if (isClassFile(name)) {
                nameList.add(toFullyQualifiedName(name, basePackage));
            } else {
                doScan(basePackage + "." + name, nameList);
            }
        }

        return nameList;
    }

    private String toFullyQualifiedName(String shortName, String basePackage) {
        StringBuilder sb = new StringBuilder(basePackage);
        sb.append('.');
        sb.append(StringUtil.trimExtension(shortName));
        //打印出结果
        System.out.println(sb.toString());
        return sb.toString();
    }

    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {

        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();
        List<String> nameList = new ArrayList<String>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
                nameList.add(name);
            }

            entry = jarIn.getNextJarEntry();
        }

        return nameList;
    }

    private List<String> readFromDirectory(String path) {
        File file = new File(path);
        String[] names = file.list();

        if (null == names) {
            return null;
        }

        return Arrays.asList(names);
    }

    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }

    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }

    /**
     * 获取所有Table注解生成Result注解的工具类
     */
    public static void main(String[] args) throws Exception {
        PackageScanner scan = new ResultAnnotationBuilder("com.prolog.eis.model.wms");
        List<String> list=scan.getFullyQualifiedClassNameList();

        for(String classStr:list){

            Class clazz=Class.forName(classStr);
          if(null!= clazz.getDeclaredAnnotation(Table.class)) {

              System.out.println("--------------------"+classStr+"-----------------------------------------");
              StringBuffer buffer=new StringBuffer("@Results(id=\""+clazz.getSimpleName()+"\" , value= { \r\n");
              Field[] declaredFields = clazz.getDeclaredFields();
              for (Field declaredField : declaredFields) {
                  Column fieldAnnotation = declaredField.getDeclaredAnnotation(Column.class);
                  if(fieldAnnotation != null){
                      String builder="  @Result(property = \""+declaredField.getName()+"\",  column = \""+fieldAnnotation.value()+"\"),\r\n";
                      buffer.append(builder);

                  }


              }


              System.out.println( StringUtils.removeEnd(buffer.toString(),",\r\n")+"\r\n    })");
          }

        }
    }
}
