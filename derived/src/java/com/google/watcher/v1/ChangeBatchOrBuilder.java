// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/watcher/v1/watch.proto

package com.google.watcher.v1;

public interface ChangeBatchOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.watcher.v1.ChangeBatch)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * A list of Change messages.
   * </pre>
   *
   * <code>repeated .google.watcher.v1.Change changes = 1;</code>
   */
  java.util.List<com.google.watcher.v1.Change> 
      getChangesList();
  /**
   * <pre>
   * A list of Change messages.
   * </pre>
   *
   * <code>repeated .google.watcher.v1.Change changes = 1;</code>
   */
  com.google.watcher.v1.Change getChanges(int index);
  /**
   * <pre>
   * A list of Change messages.
   * </pre>
   *
   * <code>repeated .google.watcher.v1.Change changes = 1;</code>
   */
  int getChangesCount();
  /**
   * <pre>
   * A list of Change messages.
   * </pre>
   *
   * <code>repeated .google.watcher.v1.Change changes = 1;</code>
   */
  java.util.List<? extends com.google.watcher.v1.ChangeOrBuilder> 
      getChangesOrBuilderList();
  /**
   * <pre>
   * A list of Change messages.
   * </pre>
   *
   * <code>repeated .google.watcher.v1.Change changes = 1;</code>
   */
  com.google.watcher.v1.ChangeOrBuilder getChangesOrBuilder(
      int index);
}
