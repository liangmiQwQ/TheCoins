package net.mirolls.thecoins.libs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialItemClickedAction {
  @JsonProperty("actionType")
  private String actionType;
  @JsonProperty("actionFunction")
  private String actionFunction;
  @JsonProperty("param")
  private String[] param;

  public SpecialItemClickedAction(String actionType, String actionFunction, String[] param) {
    this.actionType = actionType;
    this.actionFunction = actionFunction;
    this.param = param;
  }

  public SpecialItemClickedAction() {
  }

  public SpecialItemClickedAction(String actionType, String actionFunction) {
    this.actionType = actionType;
    this.actionFunction = actionFunction;
    this.param = new String[]{};
  }


  public String getActionType() {
    return actionType;
  }

  public void setActionType(String actionType) {
    this.actionType = actionType;
  }

  public String getActionFunction() {
    return actionFunction;
  }

  public void setActionFunction(String actionFunction) {
    this.actionFunction = actionFunction;
  }

  public String[] getParam() {
    return param;
  }

  public void setParam(String[] param) {
    this.param = param;
  }
}
