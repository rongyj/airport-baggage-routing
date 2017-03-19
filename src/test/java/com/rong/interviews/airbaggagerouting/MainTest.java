package com.rong.interviews.airbaggagerouting;

import com.rong.interviews.airportbaggagerouting.Main;
import junit.framework.Test;
import junit.framework.TestCase;

import java.io.File;

/**
 * Created by rongyj on 3/18/17.
 */
public class MainTest extends TestCase {

    public void testMain(){
        Main.main(new String [] {"src/test/data/InputTestData.txt"});
    }
}
