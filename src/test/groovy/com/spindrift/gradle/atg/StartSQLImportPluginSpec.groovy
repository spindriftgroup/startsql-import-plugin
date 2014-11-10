package com.spindrift.gradle.atg

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class StartSQLImportPluginSpec extends Specification {
  Project project

  def setup() {
    project = ProjectBuilder.builder().build()
  }

  def "Creates startSQLImport default task"() {
    when:
      //project.apply plugin: 'com.spindrift.startsql-import'
      project.plugins.apply(StartSQLImportPlugin.class)
    then:
      project.tasks.findByName('startSQLImport') != null
  }
}
            