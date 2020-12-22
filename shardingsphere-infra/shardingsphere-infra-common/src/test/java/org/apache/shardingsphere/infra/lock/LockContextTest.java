/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.infra.lock;

import org.apache.shardingsphere.infra.spi.ShardingSphereServiceLoader;
import org.apache.shardingsphere.infra.spi.typed.TypedSPIRegistry;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public final class LockContextTest {
    
    static {
        ShardingSphereServiceLoader.register(LockStrategy.class);
    }
    
    @Before
    public void init() {
        LockContext.init(TypedSPIRegistry.getRegisteredService(LockStrategy.class, LockStrategyType.STANDARD.name(), new Properties()));
    }
    
    @Test
    public void assertGetLockStrategy() {
        assertNotNull(LockContext.getLockStrategy());
    }
    
    @Test
    public void assetAwait() {
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(200L);
                // CHECKSTYLE:OFF
            } catch (final InterruptedException e) {
                // CHECKSTYLE:ON
            }
            LockContext.signalAll();
        }).start();
        boolean result = LockContext.await(400L);
        assertTrue(result);
    }
}
