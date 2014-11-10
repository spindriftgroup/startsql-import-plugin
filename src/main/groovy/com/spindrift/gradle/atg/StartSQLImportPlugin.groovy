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
package com.spindrift.gradle.atg

/**
 * Provides wrapper functionality for Oracle Web Commerce's startSQLImport import tool.
 * CLI Usage: startSQLImport -file <data-file> {-workspace <workspace-name> | -project <project-name>} [options]
 * 
 * @author hallatech
 */

import java.text.MessageFormat;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task;

import com.spindrift.gradle.atg.tasks.StartSQLImport

class StartSQLImportPlugin implements Plugin<Project> {
  
  static final String PLUGIN_EXTENSION_NAME="startSQLImport"
  static final String START_SQL_IMPORT_TASK="startSQLImport"

  @Override
  public void apply(Project project) {
    project.extensions."${PLUGIN_EXTENSION_NAME}" = new StartSQLImportExtension()
    addStartSQLImportTask(project)
  }
  

  private void addStartSQLImportTask(Project project) {
    project.task(START_SQL_IMPORT_TASK, type: StartSQLImport )
  }

}
