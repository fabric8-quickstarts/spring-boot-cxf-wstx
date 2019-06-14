/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2019, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package io.fabric8.quickstarts.cxf.wstx;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.UserTransaction;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleWsApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class  BridgeFromJTATest {

    @Autowired
    private UserTransaction ut;

    private FirstServiceAT firstClient;
    private SecondServiceAT secondClient;


    @Before
    public void setUp() throws Exception {
        firstClient = FirstClient.newInstance();
        secondClient = SecondClient.newInstance();
    }

    @After
    public void teardownTest() throws Exception {
        rollbackIfActive(ut);
        try {
            ut.begin();
            firstClient.resetCounter();
            secondClient.resetCounter();
            ut.commit();
        } finally {
            rollbackIfActive(ut);
        }
    }


    @Test
    public void testCommit() throws Exception {
        ut.begin();
        firstClient.incrementCounter(1);
        secondClient.incrementCounter(1);
        ut.commit();

        ut.begin();
        int counter1 = firstClient.getCounter();
        int counter2 = secondClient.getCounter();
        ut.commit();

        Assert.assertEquals(1, counter1);
        Assert.assertEquals(1, counter2);
    }

    @Test
    public void testClientDrivenRollback() throws Exception {
        ut.begin();
        firstClient.incrementCounter(1);
        secondClient.incrementCounter(1);
        ut.rollback();

        ut.begin();
        int counter1 = firstClient.getCounter();
        int counter2 = secondClient.getCounter();
        ut.commit();

        Assert.assertEquals(0, counter1);
        Assert.assertEquals(0, counter2);
    }


    /**
     * Utility method for rolling back a transaction if it is currently active.
     *
     * @param ut The User Business Activity to cancel.
     */
    private void rollbackIfActive(UserTransaction ut) {
        try {
            ut.rollback();
        } catch (Throwable th2) {
            // do nothing, not active
        }
    }
}
