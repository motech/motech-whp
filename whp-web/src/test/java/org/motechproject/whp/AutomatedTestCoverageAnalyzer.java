package org.motechproject.whp;

import org.junit.Test;
import org.motechproject.whp.common.util.SpringIntegrationTest;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AutomatedTestCoverageAnalyzer {
    static Map<String, StringBuilder> coverageMap = new HashMap<>();
    public static void main(String[] args) {
        coverageMap.put("Functional Tests" , new StringBuilder("*******************Motech-WHP Functional Tests*******************\n\n"));
        coverageMap.put("Integration Tests" , new StringBuilder("*******************Motech-WHP Integration Tests*******************\n\n"));
        coverageMap.put("Unit Tests" , new StringBuilder("*******************Motech-WHP Unit Tests*******************\n\n"));
        File testRootDirectory = new File("C:\\Users\\dheerenn\\motech-whp");
        analyzeCoverageFor(testRootDirectory);
    }

    private static void analyzeCoverageFor(File directory) {
        for(File module : directory.listFiles()) {
            if(module.getName().startsWith("whp-")) {
                appendCoverageFor(module);
            }
        }
        for(StringBuilder coverage : coverageMap.values()) {
            System.out.println(coverage.toString() + "****************************************************************************\n\n");
        }
    }
    private static void appendCoverageFor(File directory) {
        if(directory.isDirectory() && !directory.getName().equals("target")) {
            for(File file : directory.listFiles()) {
                appendCoverageFor(file);
            }
        } else {
            String path = directory.getPath();
            if(!path.contains("test"))
                return;
            try {
                int endIndex = path.indexOf("org\\");
                if(endIndex <= 0)
                    return;
                String prefix = path.substring(0, endIndex);
                String relativePath = path.replace(prefix, "");
                Class testClass = Class.forName(relativePath.replace("\\", ".").replace(".java", ""));
                String testCategory = resolveTestCategory(testClass);
                if(testCategory == null)
                    return;
                StringBuilder testMethods = new StringBuilder();
                for(Method method : testClass.getMethods()) {
                    appendCoverageIfTest(method, testMethods);
                }
                if(!testMethods.toString().isEmpty()) {
                    StringBuilder coverageBuilder = coverageMap.get(testCategory);
                    coverageBuilder.append(directory.getName() + "\n");
                    coverageBuilder.append(testMethods.toString());
                    coverageBuilder.append("\n");
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Failed to generate analysis for: " + path);
            }
        }
    }

    private static String resolveTestCategory(Class _class) {
        if(SpringIntegrationTest.class.isAssignableFrom(_class)) {
            return "Integration Tests";
        } else if(_class.getName().contains("functional")) {
            return "Functional Tests";
        } else {
            return "Unit Tests";
        }
    }

    private static void appendCoverageIfTest(Method method, StringBuilder coverageBuilder) {
        for(Annotation annotation : method.getDeclaredAnnotations()) {
            if(annotation instanceof Test) {
                coverageBuilder.append("\t" + method.getName() + "\n");
            }
        }
    }
}
