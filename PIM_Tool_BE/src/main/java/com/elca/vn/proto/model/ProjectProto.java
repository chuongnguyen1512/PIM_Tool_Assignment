// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: pim_project.proto

package com.elca.vn.proto.model;

public final class ProjectProto {
  private ProjectProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_elca_vn_model_PimProject_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_elca_vn_model_PimProject_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021pim_project.proto\022\021com.elca.vn.model\032\037" +
      "google/protobuf/timestamp.proto\"\266\002\n\nPimP" +
      "roject\022\025\n\rprojectNumber\030\002 \001(\005\022\023\n\013project" +
      "Name\030\003 \001(\t\022\020\n\010customer\030\004 \001(\t\022\017\n\007groupID\030" +
      "\005 \001(\003\022\023\n\013memberVISAs\030\006 \003(\003\022)\n\006status\030\007 \001" +
      "(\0162\031.com.elca.vn.proto.model.Status\022-\n\tstartDa" +
      "te\030\010 \001(\0132\032.google.protobuf.Timestamp\022+\n\007" +
      "endDate\030\t \001(\0132\032.google.protobuf.Timestam" +
      "p\022=\n\020processingStatus\030\n \001(\0162#.com.elca.v" +
      "n.model.ProcessingStatus*6\n\020ProcessingSt" +
      "atus\022\n\n\006INSERT\020\000\022\n\n\006UPDATE\020\001\022\n\n\006DELETE\020\002" +
      "*,\n\006Status\022\007\n\003NEW\020\000\022\007\n\003PLA\020\001\022\007\n\003INO\020\002\022\007\n" +
      "\003FIN\020\003B\026B\014ProjectProtoP\001\242\002\003RTGb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf.TimestampProto.getDescriptor(),
        });
    internal_static_com_elca_vn_model_PimProject_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_elca_vn_model_PimProject_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_elca_vn_model_PimProject_descriptor,
        new java.lang.String[] { "ProjectNumber", "ProjectName", "Customer", "GroupID", "MemberVISAs", "Status", "StartDate", "EndDate", "ProcessingStatus", });
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
