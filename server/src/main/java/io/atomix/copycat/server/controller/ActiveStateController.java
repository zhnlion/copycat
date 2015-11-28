/*
 * Copyright 2015 the original author or authors.
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
 * limitations under the License
 */
package io.atomix.copycat.server.controller;

import io.atomix.copycat.server.cluster.MemberType;
import io.atomix.copycat.server.cluster.RaftMemberType;
import io.atomix.copycat.server.state.*;

import java.util.concurrent.CompletableFuture;

/**
 * Active state controller.
 *
 * @author <a href="http://github.com/kuujo>Jordan Halterman</a>
 */
public class ActiveStateController extends RaftStateController {

  public ActiveStateController(ServerStateContext context) {
    super(context);
  }

  @Override
  public MemberType type() {
    return RaftMemberType.ACTIVE;
  }

  @Override
  public CompletableFuture<ServerStateController<RaftState>> open() {
    transition(RaftStateType.FOLLOWER);
    return super.open();
  }

  @Override
  public void transition(ServerState.Type<RaftState> state) {
    if (state != RaftStateType.INACTIVE && state != RaftStateType.FOLLOWER && state != RaftStateType.CANDIDATE && state != RaftStateType.LEADER) {
      throw new IllegalStateException("illegal active member state: " + state);
    }
    super.transition(state);
  }

  @Override
  public CompletableFuture<Void> close() {
    transition(RaftStateType.INACTIVE);
    return super.close();
  }

}
