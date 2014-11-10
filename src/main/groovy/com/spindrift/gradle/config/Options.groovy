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
package com.spindrift.gradle.config

/**
 * A container for optional parameters
 * @author hallatech
 */
import groovy.transform.AutoClone

@AutoClone
class Options {
  
  List<String> modules
  String server
  String repository
  Map<String, String> options = [:]

  void option(String key, String value) {
      options[key] = value
  }
  
  /**
   * @return a list of options broken down into option key followed by option value and where required in the order expected
   */
  public List<String> list() {
    List opts=[]
    if (modules) {
      opts << '-m'
      modules.each { opts << it }
    }
    if (server) {
      opts << '-s'
      opts << server
    }
    if (repository) {
      opts << '-repository'
      opts << repository
    }
    if (options) {
      options.each { k,v ->
        opts << k
        opts << v
      }
    }
    opts
  }
}
