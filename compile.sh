#!/bin/bash

set -eu

readonly dir=$(dirname "$0")

compile() {
  local config_script
  if [ -z $1 ]; then
    config_script=""
  else
    config_script="--configscript ${dir}/src/conf/$1.groovy"
  fi
  rm -rf ${dir}/target
  groovyc \
    ${config_script} \
    -d ${dir}/target \
    ${dir}/src/main/groovy/org/venutolo/compilestatic/*.groovy
}

echo "compilation without configscript is good"
compile ""

echo "compilation with configscript always applying @CompileStatic is good"
compile "alwaysApplyCompileStatic"

echo "compilation with configscript selectively applying @CompileStatic is bad"
compile "selectivelyApplyCompileStatic"
