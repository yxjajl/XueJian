// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: com/proto/login/PlayerCreateProto.proto

package com.dh.game.vo.login;

public final class PlayerCreateProto {
  private PlayerCreateProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface PlayerCreateRequestOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string account = 1;
    boolean hasAccount();
    String getAccount();
    
    // required string password = 2;
    boolean hasPassword();
    String getPassword();
    
    // required int32 headIcon = 3;
    boolean hasHeadIcon();
    int getHeadIcon();
    
    // required string nick = 4;
    boolean hasNick();
    String getNick();
    
    // optional string pfkey = 5 [default = "1"];
    boolean hasPfkey();
    String getPfkey();
    
    // optional string platform = 6 [default = "1"];
    boolean hasPlatform();
    String getPlatform();
  }
  public static final class PlayerCreateRequest extends
      com.google.protobuf.GeneratedMessage
      implements PlayerCreateRequestOrBuilder {
    // Use PlayerCreateRequest.newBuilder() to construct.
    private PlayerCreateRequest(Builder builder) {
      super(builder);
    }
    private PlayerCreateRequest(boolean noInit) {}
    
    private static final PlayerCreateRequest defaultInstance;
    public static PlayerCreateRequest getDefaultInstance() {
      return defaultInstance;
    }
    
    public PlayerCreateRequest getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.dh.game.vo.login.PlayerCreateProto.internal_static_com_dh_game_vo_login_PlayerCreateRequest_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.dh.game.vo.login.PlayerCreateProto.internal_static_com_dh_game_vo_login_PlayerCreateRequest_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string account = 1;
    public static final int ACCOUNT_FIELD_NUMBER = 1;
    private java.lang.Object account_;
    public boolean hasAccount() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getAccount() {
      java.lang.Object ref = account_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          account_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getAccountBytes() {
      java.lang.Object ref = account_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        account_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required string password = 2;
    public static final int PASSWORD_FIELD_NUMBER = 2;
    private java.lang.Object password_;
    public boolean hasPassword() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getPassword() {
      java.lang.Object ref = password_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          password_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getPasswordBytes() {
      java.lang.Object ref = password_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        password_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required int32 headIcon = 3;
    public static final int HEADICON_FIELD_NUMBER = 3;
    private int headIcon_;
    public boolean hasHeadIcon() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getHeadIcon() {
      return headIcon_;
    }
    
    // required string nick = 4;
    public static final int NICK_FIELD_NUMBER = 4;
    private java.lang.Object nick_;
    public boolean hasNick() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public String getNick() {
      java.lang.Object ref = nick_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          nick_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getNickBytes() {
      java.lang.Object ref = nick_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        nick_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string pfkey = 5 [default = "1"];
    public static final int PFKEY_FIELD_NUMBER = 5;
    private java.lang.Object pfkey_;
    public boolean hasPfkey() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public String getPfkey() {
      java.lang.Object ref = pfkey_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          pfkey_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getPfkeyBytes() {
      java.lang.Object ref = pfkey_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        pfkey_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string platform = 6 [default = "1"];
    public static final int PLATFORM_FIELD_NUMBER = 6;
    private java.lang.Object platform_;
    public boolean hasPlatform() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public String getPlatform() {
      java.lang.Object ref = platform_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          platform_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getPlatformBytes() {
      java.lang.Object ref = platform_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        platform_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    private void initFields() {
      account_ = "";
      password_ = "";
      headIcon_ = 0;
      nick_ = "";
      pfkey_ = "1";
      platform_ = "1";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasAccount()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasPassword()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasHeadIcon()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasNick()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getAccountBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getPasswordBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, headIcon_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, getNickBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, getPfkeyBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(6, getPlatformBytes());
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getAccountBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getPasswordBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, headIcon_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getNickBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getPfkeyBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, getPlatformBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.dh.game.vo.login.PlayerCreateProto.internal_static_com_dh_game_vo_login_PlayerCreateRequest_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.dh.game.vo.login.PlayerCreateProto.internal_static_com_dh_game_vo_login_PlayerCreateRequest_fieldAccessorTable;
      }
      
      // Construct using com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        account_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        password_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        headIcon_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        nick_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        pfkey_ = "1";
        bitField0_ = (bitField0_ & ~0x00000010);
        platform_ = "1";
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest.getDescriptor();
      }
      
      public com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest getDefaultInstanceForType() {
        return com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest.getDefaultInstance();
      }
      
      public com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest build() {
        com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest buildPartial() {
        com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest result = new com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.account_ = account_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.password_ = password_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.headIcon_ = headIcon_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.nick_ = nick_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.pfkey_ = pfkey_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.platform_ = platform_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest) {
          return mergeFrom((com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest other) {
        if (other == com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest.getDefaultInstance()) return this;
        if (other.hasAccount()) {
          setAccount(other.getAccount());
        }
        if (other.hasPassword()) {
          setPassword(other.getPassword());
        }
        if (other.hasHeadIcon()) {
          setHeadIcon(other.getHeadIcon());
        }
        if (other.hasNick()) {
          setNick(other.getNick());
        }
        if (other.hasPfkey()) {
          setPfkey(other.getPfkey());
        }
        if (other.hasPlatform()) {
          setPlatform(other.getPlatform());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasAccount()) {
          
          return false;
        }
        if (!hasPassword()) {
          
          return false;
        }
        if (!hasHeadIcon()) {
          
          return false;
        }
        if (!hasNick()) {
          
          return false;
        }
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              account_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              password_ = input.readBytes();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              headIcon_ = input.readInt32();
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              nick_ = input.readBytes();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              pfkey_ = input.readBytes();
              break;
            }
            case 50: {
              bitField0_ |= 0x00000020;
              platform_ = input.readBytes();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string account = 1;
      private java.lang.Object account_ = "";
      public boolean hasAccount() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getAccount() {
        java.lang.Object ref = account_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          account_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setAccount(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        account_ = value;
        onChanged();
        return this;
      }
      public Builder clearAccount() {
        bitField0_ = (bitField0_ & ~0x00000001);
        account_ = getDefaultInstance().getAccount();
        onChanged();
        return this;
      }
      void setAccount(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        account_ = value;
        onChanged();
      }
      
      // required string password = 2;
      private java.lang.Object password_ = "";
      public boolean hasPassword() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getPassword() {
        java.lang.Object ref = password_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          password_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setPassword(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        password_ = value;
        onChanged();
        return this;
      }
      public Builder clearPassword() {
        bitField0_ = (bitField0_ & ~0x00000002);
        password_ = getDefaultInstance().getPassword();
        onChanged();
        return this;
      }
      void setPassword(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        password_ = value;
        onChanged();
      }
      
      // required int32 headIcon = 3;
      private int headIcon_ ;
      public boolean hasHeadIcon() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getHeadIcon() {
        return headIcon_;
      }
      public Builder setHeadIcon(int value) {
        bitField0_ |= 0x00000004;
        headIcon_ = value;
        onChanged();
        return this;
      }
      public Builder clearHeadIcon() {
        bitField0_ = (bitField0_ & ~0x00000004);
        headIcon_ = 0;
        onChanged();
        return this;
      }
      
      // required string nick = 4;
      private java.lang.Object nick_ = "";
      public boolean hasNick() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getNick() {
        java.lang.Object ref = nick_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          nick_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setNick(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        nick_ = value;
        onChanged();
        return this;
      }
      public Builder clearNick() {
        bitField0_ = (bitField0_ & ~0x00000008);
        nick_ = getDefaultInstance().getNick();
        onChanged();
        return this;
      }
      void setNick(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        nick_ = value;
        onChanged();
      }
      
      // optional string pfkey = 5 [default = "1"];
      private java.lang.Object pfkey_ = "1";
      public boolean hasPfkey() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public String getPfkey() {
        java.lang.Object ref = pfkey_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          pfkey_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setPfkey(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        pfkey_ = value;
        onChanged();
        return this;
      }
      public Builder clearPfkey() {
        bitField0_ = (bitField0_ & ~0x00000010);
        pfkey_ = getDefaultInstance().getPfkey();
        onChanged();
        return this;
      }
      void setPfkey(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000010;
        pfkey_ = value;
        onChanged();
      }
      
      // optional string platform = 6 [default = "1"];
      private java.lang.Object platform_ = "1";
      public boolean hasPlatform() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public String getPlatform() {
        java.lang.Object ref = platform_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          platform_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setPlatform(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        platform_ = value;
        onChanged();
        return this;
      }
      public Builder clearPlatform() {
        bitField0_ = (bitField0_ & ~0x00000020);
        platform_ = getDefaultInstance().getPlatform();
        onChanged();
        return this;
      }
      void setPlatform(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000020;
        platform_ = value;
        onChanged();
      }
      
      // @@protoc_insertion_point(builder_scope:com.dh.game.vo.login.PlayerCreateRequest)
    }
    
    static {
      defaultInstance = new PlayerCreateRequest(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:com.dh.game.vo.login.PlayerCreateRequest)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_com_dh_game_vo_login_PlayerCreateRequest_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_com_dh_game_vo_login_PlayerCreateRequest_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\'com/proto/login/PlayerCreateProto.prot" +
      "o\022\024com.dh.game.vo.login\"\177\n\023PlayerCreateR" +
      "equest\022\017\n\007account\030\001 \002(\t\022\020\n\010password\030\002 \002(" +
      "\t\022\020\n\010headIcon\030\003 \002(\005\022\014\n\004nick\030\004 \002(\t\022\020\n\005pfk" +
      "ey\030\005 \001(\t:\0011\022\023\n\010platform\030\006 \001(\t:\0011"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_com_dh_game_vo_login_PlayerCreateRequest_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_com_dh_game_vo_login_PlayerCreateRequest_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_com_dh_game_vo_login_PlayerCreateRequest_descriptor,
              new java.lang.String[] { "Account", "Password", "HeadIcon", "Nick", "Pfkey", "Platform", },
              com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest.class,
              com.dh.game.vo.login.PlayerCreateProto.PlayerCreateRequest.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}