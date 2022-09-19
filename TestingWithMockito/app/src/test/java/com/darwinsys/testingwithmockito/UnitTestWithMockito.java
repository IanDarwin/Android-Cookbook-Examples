package com.darwinsys.testingwithmockito;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * A local unit test with Mockito, to run on the development machine (host).
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class UnitTestWithMockito {
    @Mock WorkerHelper helper;

    @Test
    public void process_calls_helper() {
        WorkerBee bee = new WorkerBee();
        bee.setHelper(helper);
        bee.process();
        verify(helper).invoke();
    }
}