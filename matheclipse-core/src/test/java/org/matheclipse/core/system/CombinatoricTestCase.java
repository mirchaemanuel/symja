package org.matheclipse.core.system;

import org.matheclipse.core.system.AbstractTestCase;

/**
 * Tests for combinatorial functions
 *
 */
public class CombinatoricTestCase extends AbstractTestCase {
  public CombinatoricTestCase(String name) {
    super(name);
  }

  /**
   * Test combinatorial functions
   */
  public void testCombinatoric() {
  	check(
      "KOrderlessPartitions[a+b+b+c+d,3]",
      "{{a,2*b,c+d},{a,2*b+c,d},{a+2*b,c,d},{a,2*b+d,c},{a+2*b,d,c},{a,c,2*b+d},{a+c,2*b,d},{a,c+d,\n" + 
      "2*b},{a+c,d,2*b},{a,d,2*b+c},{a+d,2*b,c},{a+d,c,2*b},{2*b,a,c+d},{2*b,a+c,d},{2*b,a+d,c},{\n" + 
      "2*b,c,a+d},{2*b+c,a,d},{2*b,c+d,a},{2*b+c,d,a},{2*b,d,a+c},{2*b+d,a,c},{2*b+d,c,a},{c,a,\n" + 
      "2*b+d},{c,a+2*b,d},{c,a+d,2*b},{c,2*b,a+d},{c,2*b+d,a},{c,d,a+2*b},{c+d,a,2*b},{c+d,\n" + 
      "2*b,a},{d,a,2*b+c},{d,a+2*b,c},{d,a+c,2*b},{d,2*b,a+c},{d,2*b+c,a},{d,c,a+2*b}}");

    check(
      "KPartitions[{a,b,c,d,e},3]",
      "{{{a},{b},{c,d,e}},{{a},{b,c},{d,e}},{{a},{b,c,d},{e}},{{a,b},{c},{d,e}},{{a,b},{c,d},{e}},{{a,b,c},{d},{e}}}");
   
    check("NumberPartitions[3]", "{{3},{2,1},{1,1,1}}");

    check(
      "Permutations[{1,2,2,3}]",
      "{{1,2,2,3},{1,2,3,2},{1,3,2,2},{2,1,2,3},{2,1,3,2},{2,2,1,3},{2,2,3,1},{2,3,1,2},{\n" + 
      "2,3,2,1},{3,1,2,2},{3,2,1,2},{3,2,2,1}}");
    check("Partition[{a,b,c,d,e,f,g},3,2]", "{{a,b,c},{c,d,e},{e,f,g}}");
    
    check("Subsets[{a,b,c,d},3]", "{{},{a},{b},{c},{d},{a,b},{a,c},{a,d},{b,c},{b,d},{c,d},{a,b,c},{a,b,d},{a,c,d},{b,c,d}}");
    check("Subsets[{a,b,c,d},2]", "{{},{a},{b},{c},{d},{a,b},{a,c},{a,d},{b,c},{b,d},{c,d}}");
    check("Subsets[{a,b,c,d},{2,3}]", "{{a,b},{a,c},{a,d},{b,c},{b,d},{c,d},{a,b,c},{a,b,d},{a,c,d},{b,c,d}}");
    check("Subsets[{a,b,c,d},{2}]", "{{a,b},{a,c},{a,d},{b,c},{b,d},{c,d}}");
    check("Subsets[{a,b,c}]", "{{a},{b},{c},{a,b},{a,c},{b,c},{a,b,c}}");
  }
}
