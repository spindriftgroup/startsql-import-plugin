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
package com.spindrift.gradle.atg.tasks

import com.spindrift.gradle.config.ScriptConfiguration;
import com.spindrift.gradle.process.ExecutionResult
import com.spindrift.gradle.process.Executor
import com.spindrift.gradle.process.ScriptExecutor

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.Input
import org.gradle.api.GradleException

/**
 * Task for executing startSQLImport into a versioned repository.
 * It iterates over a list of script configurations which define the parameters for each import.
 * @author hallatech
 *
 */
public class StartSQLImport extends DefaultTask {
  
  static final String TASK_DESCRIPTION = "Imports repository data from an ATG style XML data file."
  static final String TASK_GROUP = "Data Management"
  
  Executor executor
  
  StartSQLImport() {
    description = TASK_DESCRIPTION
    group = TASK_GROUP
    executor = new ScriptExecutor()
  }
  
  @TaskAction
  public void executeCommandLine() {
    ExecutionResult result
    project.startSQLImport.imports.configurations.each { script ->
      project.logger.lifecycle "Executing: ${script}"
      
      def before = System.currentTimeMillis()
      result = executor.execute(script)
      def after = System.currentTimeMillis()
      double timeInSeconds = (after - before) / 1000D 
      
      if (result.isSuccessful()) {
        project.logger.lifecycle "Imported ${script.file} in ${timeInSeconds} secs"
        if (result.hasOutput()) {
          project.logger.info result.output
        }
      }
      else {
        String errorOutput = "${result.output}\n${result.errorText}"
        throw new GradleException("Script execution failed with code:${result.exitValue}\n${errorOutput}")
      }
    }
  }
}
