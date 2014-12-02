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
  static final String MARK_LICENSE_AS_READ_TASK="markLicenseAsRead"

  @Override
  public void apply(Project project) {
    project.extensions."${PLUGIN_EXTENSION_NAME}" = new StartSQLImportExtension()
    addStartSQLImportTask(project)
    addMarkLicenseAsReadTask(project)
  }
  

  private void addStartSQLImportTask(Project project) {
    project.task(START_SQL_IMPORT_TASK, type: StartSQLImport )
  }

  /**
   * startSQLImport requires a license read response on first execution.
   * Piping a yes to the startSQLImport task causes an endless loop, so we simply
   * create the file with a 'y' as would be done by the check itself.
   * @param project
   */
  private void addMarkLicenseAsReadTask(Project project) {
    Task task = project.getTasks().create(MARK_LICENSE_AS_READ_TASK)
    task.description="Set the startSQLImport license check as read"
    task.group="Data Management"
    task.doLast {
      def atgHome=System.env['ATG_HOME']
      assert atgHome, "Expected ATG_HOME environment variable is invalid."
      File atgHomeDir = new File(atgHome)
      assert atgHomeDir.exists(), "[$atgHome] directory does not exist. Ensure ATG_HOME points to a valid ATG installation"
      File dynHomeDir = new File(atgHome,"home")
      assert dynHomeDir.exists()
      File licenseReadFile = new File(dynHomeDir,'license.read')
      licenseReadFile.withWriter { w->
        w << 'y'
      }
    }
    project.tasks."${START_SQL_IMPORT_TASK}".dependsOn "${MARK_LICENSE_AS_READ_TASK}"
  }
}
