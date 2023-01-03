# Distributed under the OSI-approved BSD 3-Clause License.  See accompanying
# file Copyright.txt or https://cmake.org/licensing for details.

cmake_minimum_required(VERSION 3.18.1-g262b901)

# We name the project and the target for the ExternalProject_Add() call
# to something that will highlight to the user what we are working on if
# something goes wrong and an error message is produced.

project(googletest-populate NONE)

include(ExternalProject)
ExternalProject_Add(googletest-populate
                     "UPDATE_DISCONNECTED" "False" "GIT_REPOSITORY" "https://github.com/google/googletest.git" "GIT_TAG" "main"
                    SOURCE_DIR          "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/6j6665j5/armeabi-v7a/_deps/googletest-src"
                    BINARY_DIR          "/home/jesper/Documents/gitz/Android-GSDemo-GoogleMap/GSDemo/app/.cxx/Debug/6j6665j5/armeabi-v7a/_deps/googletest-build"
                    CONFIGURE_COMMAND   ""
                    BUILD_COMMAND       ""
                    INSTALL_COMMAND     ""
                    TEST_COMMAND        ""
                    USES_TERMINAL_DOWNLOAD  YES
                    USES_TERMINAL_UPDATE    YES
)


