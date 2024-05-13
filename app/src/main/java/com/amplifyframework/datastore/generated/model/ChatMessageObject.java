package com.amplifyframework.datastore.generated.model;


import androidx.core.util.ObjectsCompat;

import java.util.Objects;
import java.util.List;

/** This is an auto generated class representing the ChatMessageObject type in your schema. */
public final class ChatMessageObject {
  private final String role;
  private final String content;
  public String getRole() {
      return role;
  }
  
  public String getContent() {
      return content;
  }
  
  private ChatMessageObject(String role, String content) {
    this.role = role;
    this.content = content;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      ChatMessageObject chatMessageObject = (ChatMessageObject) obj;
      return ObjectsCompat.equals(getRole(), chatMessageObject.getRole()) &&
              ObjectsCompat.equals(getContent(), chatMessageObject.getContent());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getRole())
      .append(getContent())
      .toString()
      .hashCode();
  }
  
  public static RoleStep builder() {
      return new Builder();
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(role,
      content);
  }
  public interface RoleStep {
    ContentStep role(String role);
  }
  

  public interface ContentStep {
    BuildStep content(String content);
  }
  

  public interface BuildStep {
    ChatMessageObject build();
  }
  

  public static class Builder implements RoleStep, ContentStep, BuildStep {
    private String role;
    private String content;
    public Builder() {
      
    }
    
    private Builder(String role, String content) {
      this.role = role;
      this.content = content;
    }
    
    @Override
     public ChatMessageObject build() {
        
        return new ChatMessageObject(
          role,
          content);
    }
    
    @Override
     public ContentStep role(String role) {
        Objects.requireNonNull(role);
        this.role = role;
        return this;
    }
    
    @Override
     public BuildStep content(String content) {
        Objects.requireNonNull(content);
        this.content = content;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String role, String content) {
      super(role, content);
      Objects.requireNonNull(role);
      Objects.requireNonNull(content);
    }
    
    @Override
     public CopyOfBuilder role(String role) {
      return (CopyOfBuilder) super.role(role);
    }
    
    @Override
     public CopyOfBuilder content(String content) {
      return (CopyOfBuilder) super.content(content);
    }
  }
  
}
