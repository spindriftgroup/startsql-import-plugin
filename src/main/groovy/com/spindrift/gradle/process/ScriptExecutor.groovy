/**
 * Copyright 2012 Spindrift
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
 */
package com.spindrift.gradle.process

import java.io.IOException
import static org.gradle.api.logging.Logging.getLogger

class ScriptExecutor implements Executor {
  private static final String LOGGER='system.out'
  
  ScriptConfiguration scriptConfiguration
  
  @Override
  ExecutionResult execute(ScriptConfiguration script) {
    scriptConfiguration = script
    Process process = scriptConfiguration.commandLine().execute()
    handleProcessExecution(process)
  }
  
  private handleProcessExecution(Process process) {
    process.waitFor()
    if (process.exitValue()!=0) {
      getLogger(LOGGER).error "[ERROR] Script execution failed with code:${process.exitValue()} and script parameters:${scriptConfiguration.commandLine()}"
    }
    new ExecutionResult(exitValue:process.exitValue(), output:process.in.text, errorText:process.err.text)
  }

}
