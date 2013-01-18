/*
 * Copyright (c) 2008-2010, Hazel Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hazelcast.map.client;

import com.hazelcast.client.ClientCommandHandler;
import com.hazelcast.instance.Node;
import com.hazelcast.map.MapService;
import com.hazelcast.map.proxy.DataMapProxy;
import com.hazelcast.nio.Protocol;
import com.hazelcast.nio.serialization.Data;
import com.sun.xml.internal.txw2.DatatypeWriter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MapTryRemoveHandler extends MapCommandHandler{
    public MapTryRemoveHandler(MapService mapService) {
        super(mapService);
    }

    @Override
    public Protocol processCall(Node node, Protocol protocol) {
        String[] args = protocol.args;
        String name = protocol.args[0];
        Data key = protocol.buffers[0];
        final long ttl = Long.valueOf(args[1]);
        DataMapProxy dataMapProxy = (DataMapProxy) mapService.createDistributedObjectForClient(name);
        try {
            Data value = dataMapProxy.tryRemove(key, ttl, TimeUnit.MILLISECONDS);
            return protocol.success(value);
        } catch (TimeoutException e) {
            return protocol.success("timeout");
        }
    }
}
