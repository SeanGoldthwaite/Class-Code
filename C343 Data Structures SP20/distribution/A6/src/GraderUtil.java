import java.util.LinkedList;
import java.lang.annotation.*;
import java.lang.reflect.*;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.Request;
import org.junit.runner.notification.Failure;
import org.junit.Test;
import org.junit.internal.RealSystem;
import java.util.Optional;
import java.io.*;

/**
 * Utility to extract all annotated tests from a list of test suites.
 * Usage : GraderUtil ClassA ClassB ClassC ... 
 * Output: Result of running each annotated test case in ClassA, ClassB, ClassC...
 *
 * @author Turab Ali Jafri
 * @date January 22 2020
 */
public class GraderUtil {

  final static String T_ANN_STR = "@org.junit.Test(";
  final static String TC_ANN_STR = "@TestCase(";
  final static long T_TIMEOUT_DEF = 0L;
  final static int MAX_SCORE = 7;
  final static PrintStream out = System.out;

  //////////////////////////////////////////
  // Objects to represent processed files //
  //////////////////////////////////////////
  
  private static class AssignmentTestSuite {
    public LinkedList<TestSuite> testSuites = new LinkedList<>();
    public int totalScore;

    public AssignmentTestSuite(LinkedList<TestSuite> ts, int sc) {
      testSuites = ts;
      totalScore = sc;
    }
  }

  private static class TestSuite {
    public Class<?> suite;
    public LinkedList<ScoredTest> tests = new LinkedList<>();
    public int totalScore;

    public TestSuite(Class<?> s, LinkedList<ScoredTest> ts, int sc) {
      suite      = s;
      tests      = ts;
      totalScore = sc;
    }
  }

  private static class ScoredTest {
    public Method test;
    public TestCase testInfo;

    public ScoredTest(Method m, TestCase tI) {
      test     = m;
      testInfo = tI;
    }
  }

  //////////////////////////////////////////////
  // Objects to represent graded test results //
  //////////////////////////////////////////////
  //TODO: be an adult and avoid using + like a baby for string-append.


  private static class CaseResult {
    String name = "";
    boolean passed;
    String error = "";
    int totalScore;
    int score;

    public CaseResult(boolean p, String e, int s) {
      passed = p;
      error  = e;
      score  = s;
    }
    public CaseResult() {}


    public String toString() {
      String PB = "#8DF589"; //pass background
      String FB = "#EC8282"; //fail background
      String MB = "#333335"; //text background
      String HC = "#FF961E"; //header font color
      String textOnLightBgColor = "#262628";
      String TP = "<table style=\"width:100%\">" +
                  "<tr><td style=\"padding:10px;background-color:" + (passed ? PB : FB) + "\">" +
                  "<b style=\"color: " + textOnLightBgColor + "\">" + name + ": " + score + "/" + totalScore + "</b><br/></td></tr>";
      String errorS = "";
      if (!passed) {
        error = error.replace("<", "&lt").replace(">", "&gt");
        errorS = "<tr><td style=\"background-color:"+MB+"; \"><p style=\"color:" + HC + " ; \">Failure message:</p><br/>" +
                 "<pre>" + error + "</pre></td></tr>";
      }
      return TP + errorS + "</table>" ;
    }
  }

  private static class TestResult {
    String name = "";
    int totalTests;
    int passedTests;
    int failedTests;
    int totalScore;
    int actualScore;
    LinkedList<CaseResult> subTests = new LinkedList<>();
    public TestResult(String n, int tT, int pT, int tS, int aS, LinkedList<CaseResult> sT) {
      name        = n;
      totalTests  = tT;
      passedTests = pT;
      failedTests = tT - pT;
      totalScore  = tS;
      actualScore = aS;
      subTests    = sT;
    }

    public String toString() {
      String headerStyle = "\"color: #FF961E\"";
      String TBG = "#262628"; //table background
      String header = "<span style=" + headerStyle + ">" +  name + ": " + "</span>" + actualScore + "/" + totalScore;
      String passed = "\nPassed Tests : " + passedTests + "/" + totalTests;
      String TP  = "<table style=\"width:100%;border:0px;background-color:" + TBG + "\">" +
                   "<tr><td style=\"border:0px\">" + 
                   "<h3>" + header + "</h3><h4>" + passed + "</h4></tr></td><tr><td style=\"border:0px\">";
      String BO  = "</tr></td></table>";
      String subTables = "";
      for(CaseResult cr : subTests) {
        subTables += cr.toString() + "<br/><br/>";
      }
      return TP + subTables + BO + "\n";
    }
  }

  //////////////////////////////////////////
  //      Objects for other shtuff        //
  //////////////////////////////////////////
  private static class Pair<A, B> {
    public A fst;
    public B snd;
    public Pair(A a, B b) {fst = a; snd = b;}
  }


  //////////////////////////////////////
  //           Utilities              //
  //////////////////////////////////////

  /**
   * Given a list of class names, fetches all methods that are annotated by TestCase,
   * and return an AssignmentTestSuite
   */
  private static Pair<AssignmentTestSuite, String> fetchTestSuite(String[] testSuites) {
    LinkedList<TestSuite> tests = new LinkedList<>();
    int totalScore = 0;
    String warnings = "";

    for (String clss : testSuites) {
      Class<?> cls = null;
      LinkedList<ScoredTest> cases = new LinkedList<>();
      boolean containsTests = false;
      int score = 0;
      try {
        cls = Class.forName(clss);
      } catch (ClassNotFoundException e) {
        System.err.println(clss + " does not exist!");
        System.exit(1);
      }
      Method[] methods = {};
      methods = cls.getDeclaredMethods();
      for (Method method : methods) {
        Annotation[] annotations = method.getAnnotations();
        Optional<Test> testAnnotation = Optional.empty();
        Optional<TestCase> testCaseAnnotation = Optional.empty();
        for (Annotation annotation : annotations) {
          String annotationStr = annotation.toString();
          // if test is annotated by Test
          if (annotationStr.length() >= T_ANN_STR.length() &&
              annotationStr.substring(0, T_ANN_STR.length()).equals(T_ANN_STR)) { 
            testAnnotation = Optional.of(method.getAnnotation(Test.class));
              }
          // if test is annotated by TestCase
          if (annotationStr.length() >= TC_ANN_STR.length() &&
              annotationStr.substring(0, TC_ANN_STR.length()).equals(TC_ANN_STR)) { 
            testCaseAnnotation = Optional.of(method.getAnnotation(TestCase.class));
              }
        }
        if (testAnnotation.isPresent() && testCaseAnnotation.isPresent()) {
          containsTests = true;
          cases.add(new ScoredTest(method, testCaseAnnotation.get()));
          score += testCaseAnnotation.get().score();
          warnings += testAnnotation.get().timeout() == T_TIMEOUT_DEF ?
                      "Warning: no timeout set for " + method.getName() + " test in class " + cls.getSimpleName() +
                      ". Make sure this is non-zero to avoid infinite loops\n\n" : "";
        } else if (testAnnotation.isPresent()) {
          warnings += "Warning: no TestCase annotation for " + method.getName() + " test in class " + cls.getSimpleName() +
                      ". Make sure this is set so the Autograder can detect this test\n\n";
        } else if (testCaseAnnotation.isPresent()) {
          warnings += "Warning: TestCase annotation for " + method.getName() + " in class " + cls.getSimpleName() +
                      " although it doesn't have a Test annotation. Make sure this is set so JUnit can detect this test\n\n";
        }
      }
      if(containsTests) {
        tests.add(new TestSuite(cls, cases, score));
        totalScore += score;
      }
    }
    return new Pair<>(new AssignmentTestSuite(tests, totalScore), warnings);
  }

  /**
   * Run the given test `m` in class `c` and returns its TestResult
   */
  private static CaseResult gradeTest(Class<?> c, Method m, String name, int score) {
    Request request = Request.method(c,m.getName());
    CaseResult res = new CaseResult();
    res.name = name; 
    res.totalScore = score;

    Result result = new JUnitCore().run(request);
    if(result.wasSuccessful()){
      res.score = score;
    } else {
      Failure f = result.getFailures().get(0);
      String exceptionName = f.getException().getClass().getSimpleName();
      String exceptionMessage = f.getException().getMessage() == null? "" : f.getException().getMessage();
      String errorMessage = exceptionName.equals("AssertionError")? "Test Failed: " + exceptionMessage :
        exceptionName + " " + exceptionMessage;        
      res.error = errorMessage;
    }
    res.passed = result.wasSuccessful();
    return res;
  }

  public static void main(String... args) {
    String[] santizedArgs = new String[args.length];

    // piping out sysouts to garbage to avoid student inputs being printed
    System.setOut(new PrintStream(new OutputStream() {
      public void write(int b) {
        //DO NOTHING
      }
    }));

    for (int i = 0; i < args.length; ++i) { // Remove .class from class names
      String[] splitS = args[i].split("\\.");
      santizedArgs[i] = splitS[0];
    }
    Pair<AssignmentTestSuite, String> assignmentTestSuiteAwarnings = fetchTestSuite(santizedArgs); // get mapping of test suites to methods
    AssignmentTestSuite assignmentTestSuite = assignmentTestSuiteAwarnings.fst;
    String warnings = assignmentTestSuiteAwarnings.snd;
    LinkedList<TestResult> finalResults = new LinkedList<>();
    int actualScore = 0;
    for (TestSuite ts : assignmentTestSuite.testSuites) { // get results of running 343 tests
      LinkedList<ScoredTest> tests       = ts.tests;
      LinkedList<CaseResult> scoredTests = new LinkedList<>(); 
      int tsAScore = 0; 
      int passedTests    = 0;
      for (ScoredTest test : tests) {
        String testName = test.testInfo.name().equals("") ? test.test.getName() : test.testInfo.name();
        CaseResult result = gradeTest(ts.suite, test.test, testName, test.testInfo.score());
        tsAScore += result.score;
        passedTests += result.passed ? 1 : 0;
        scoredTests.add(result);
      }
      finalResults.add(new TestResult(ts.suite.getSimpleName(), ts.tests.size(), passedTests, ts.totalScore, tsAScore, scoredTests));
      actualScore += tsAScore;
    }
    //////////////////////////////////////
    //           Percentage             //
    //////////////////////////////////////
    float perc = (actualScore * MAX_SCORE) / ((float) assignmentTestSuite.totalScore);
    //out.println(Math.round(perc));
    out.printf("%.2f\n", perc);
    //////////////////////////////////////
    //           Printing               //
    //////////////////////////////////////
    String BG = "#333335"; //main background
    String FC = "#C3C3C5"; //main font color
    String HC = "#FF961E"; //header color; orange
    String FO = "monospace";
    String FS = "18";
    String TP = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/><title>Submission Report</title>" +
      "<style type=\"text/css\"> table,td { border: 0px; border-radius: 3px; padding: 3px; }</style></head>" +
      "<body style=\"background-color:" + BG + "; font-family:" + FO + "; font-size:" + FO + "; color: " + FC + "; \">" +
      "<h1 style=\"color:" + HC + "\">Submission Report:</h1>" +
      "<h1>Score: " + actualScore + "/" + assignmentTestSuite.totalScore + "  →  " + String.format("%.2f", perc) + "/7.00</h1>";
    String BO = "</body></html>";

    String warningString = warnings.length() != 0 ? "<h1 style=\"color: red\">Warnings</h1><pre>" + warnings + "</pre>" : "";


    String separator = "<br/><br/>";
    String details = "";
    for (TestResult ts : finalResults) {
      details += ts.toString() + separator;
    }
    out.println(TP + warningString + details + BO);
  }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface TestCase {
int score();
String author();
String name() default ""; // A better name for the test case. Optional
}
