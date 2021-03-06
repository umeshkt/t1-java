/*******************************************************************************
 * Copyright 2016 MediaMath
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.mediamath.terminalone.models.helper;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.ws.rs.core.Form;

import com.mediamath.terminalone.models.Segments;
import com.mediamath.terminalone.models.Strategy;
import com.mediamath.terminalone.models.Strategy.goalType;
import com.mediamath.terminalone.models.StrategyDomain;
import com.mediamath.terminalone.models.TargetValues;
import com.mediamath.terminalone.utils.Utility;

public class StrategyHelper {

  private StrategyHelper() {
    throw new IllegalAccessError("StrategyHelper cannot be instantiated");
  }

  /**
   * creates a Strategy Form object.
   * 
   * @param entity
   *          expects a Strategy entity.
   * @return Form object.
   */
  public static Form getForm(Strategy entity) {
    final String YYYY_MM_DDTHH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ssZ";
    final SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DDTHH_MM_SS_Z);

    Form strategyForm = new Form();
    if (entity.getStrategyDomainRestrictions().isEmpty()) {

      if (entity.getAudienceSegmentExcludeOp() != null && entity.getAudienceSegments().isEmpty()) {
        strategyForm.param("audience_segment_exclude_op",
            entity.getAudienceSegmentExcludeOp().toString());
      } else if (entity.getAudienceSegmentExcludeOp() != null
          && !entity.getAudienceSegments().isEmpty()) {
        strategyForm.param("exclude_op", entity.getAudienceSegmentExcludeOp().toString());
      }

      if (entity.getAudienceSegmentIncludeOp() != null && entity.getAudienceSegments().isEmpty()) {
        strategyForm.param("audience_segment_include_op",
            entity.getAudienceSegmentIncludeOp().toString());
      } else if (entity.getAudienceSegmentIncludeOp() != null
          && !entity.getAudienceSegments().isEmpty()) {
        strategyForm.param("include_op", entity.getAudienceSegmentIncludeOp().toString());
      }

      if (entity.getBidAggresiveness() > 0f) {
        strategyForm.param("bid_aggressiveness", String.valueOf(entity.getBidAggresiveness()));
      }

      strategyForm.param("bid_price_is_media_only",
          Utility.getOneOrZero(entity.isBidPriceIsMediaOnly()));

      if (entity.getBudget() != null && !entity.getBudget().isEmpty()
          && entity.getBudget().get(0).getValue() > 0) {
        strategyForm.param("budget", String.valueOf(entity.getBudget().get(0).getValue()));
      }
      if (entity.getCampaignId() > 0) {
        strategyForm.param("campaign_id", String.valueOf(entity.getCampaignId()));
      }

      if (entity.getCreatedOn() != null) {
        strategyForm.param("created_on", entity.getCreatedOn());
      }

      if (entity.getDescription() != null) {
        strategyForm.param("description", entity.getDescription());
      }

      if (entity.getEffectiveGoalValue() != null && !entity.getEffectiveGoalValue().isEmpty()
          && entity.getEffectiveGoalValue().get(0).getValue() > 0) {
        strategyForm.param("effective_goal_value",
            String.valueOf(entity.getEffectiveGoalValue().get(0).getValue()));
      }

      if (entity.getFrequencyType() != null) {
        strategyForm.param("frequency_type", entity.getFrequencyType().toString());
      }

      if (entity.getFrequencyInterval() != null) {
        strategyForm.param("frequency_interval", String.valueOf(entity.getFrequencyInterval()));
      }

      if (entity.getFrequencyAmount() > 0) {
        strategyForm.param("frequency_amount", String.valueOf(entity.getFrequencyAmount()));
      }

      if (entity.getEndDate() != null && !entity.isUseCampaignEnd()) {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String endDate = sdf.format(entity.getEndDate());
        strategyForm.param("end_date", endDate);
      }

      if (!entity.isUseCampaignStart() && entity.getStartDate() != null) {
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String startDate = sdf.format(entity.getStartDate());
        strategyForm.param("start_date", startDate);
      }

      if (entity.getGoalType() != null) {
        strategyForm.param("goal_type", String.valueOf(entity.getGoalType()));
      }

      if (entity.getGoalType() != null && !entity.getGoalValue().isEmpty()
          && entity.getGoalValue().get(0).getValue() > 0) {
        strategyForm.param("goal_value", String.valueOf(entity.getGoalValue().get(0).getValue()));
      }

      if (entity.getMaxBid() != null && !entity.getMaxBid().isEmpty()
          && entity.getMaxBid().get(0).getValue() > 0) {
        strategyForm.param("max_bid", String.valueOf(entity.getMaxBid().get(0).getValue()));
      }

      if (entity.getMediaType() != null) {
        strategyForm.param("media_type", String.valueOf(entity.getMediaType()));
      }
      if (entity.getName() != null) {
        strategyForm.param("name", entity.getName());
      }

      if (entity.getPacingAmount() != null && !entity.getPacingAmount().isEmpty()
          && entity.getPacingAmount().get(0).getValue() > 0) {
        strategyForm.param("pacing_amount",
            String.valueOf(entity.getPacingAmount().get(0).getValue()));
      }
      if (entity.getPacingInterval() != null) {
        strategyForm.param("pacing_interval", String.valueOf(entity.getPacingInterval()));
      }
      if (entity.getPacingType() != null) {
        strategyForm.param("pacing_type", String.valueOf(entity.getPacingType()));
      }

      if (entity.getImpressionCapType() != null) {
        strategyForm.param("impression_cap_type", String.valueOf(entity.getImpressionCapType()));
      }

      if (entity.getImpressionPacingInterval() != null) {
        strategyForm.param("impression_pacing_interval",
            String.valueOf(entity.getImpressionPacingInterval()));
      }

      if (entity.getImpressionPacingAmount() > 0) {
        strategyForm.param("impression_pacing_amount",
            String.valueOf(entity.getImpressionPacingAmount()));
      }

      StringBuilder pixelTargetExpression = new StringBuilder();
      StringBuilder includePixels = new StringBuilder();
      StringBuilder excludePixels = new StringBuilder();

      if (entity.getIncludePixels() != null && !entity.getIncludePixels().isEmpty()) {
        includePixels.append("(");
        int size = entity.getIncludePixels().size() - 1;
        for (Integer i : entity.getIncludePixels()) {
          if (size != 0) {
            includePixels.append("[" + i + "] AND ");
            size--;
          } else {
            includePixels.append("[" + i + "]");
          }
        }
        includePixels.append(")");
        pixelTargetExpression.append(includePixels.toString());
      }

      if (entity.getExcludePixels() != null && !entity.getExcludePixels().isEmpty()) {
        excludePixels.append("(");
        int size = entity.getExcludePixels().size() - 1;
        for (Integer i : entity.getExcludePixels()) {
          if (size != 0) {
            excludePixels.append("[" + i + "] OR ");
            size--;
          } else {
            excludePixels.append("[" + i + "]");
          }
        }
        excludePixels.append(")");

        if (pixelTargetExpression.length() > 0) {
          pixelTargetExpression.append(" AND NOT " + excludePixels.toString());
        }
      }

      if (pixelTargetExpression.length() > 0) {
        strategyForm.param("pixel_target_expr", pixelTargetExpression.toString());
      }

      if (entity.getRoiTarget() != null && !entity.getRoiTarget().isEmpty()
          && entity.getRoiTarget().get(0).getValue() > 0) {
        strategyForm.param("roi_target", String.valueOf(entity.getRoiTarget().get(0).getValue()));
      }

      strategyForm.param("run_on_all_exchanges", Utility.getOnOrOff(entity.isRunOnAllExchanges()));
      strategyForm.param("run_on_all_pmp", Utility.getOnOrOff(entity.isRunOnAllPmp()));
      strategyForm.param("run_on_display", Utility.getOnOrOff(entity.isRunOnDisplay()));
      strategyForm.param("run_on_mobile", Utility.getOnOrOff(entity.isRunOnMobile()));
      strategyForm.param("run_on_streaming", Utility.getOnOrOff(entity.isRunOnStreaming()));
      if (entity.getSiteSelectiveness() != null) {
        strategyForm.param("site_selectiveness", String.valueOf(entity.getSiteSelectiveness()));
      }

      strategyForm.param("site_restriction_transparent_urls",
          Utility.getOnOrOff(entity.isSiteRestrictionTransparentUrls()));
      strategyForm.param("use_campaign_start", Utility.getOnOrOff(entity.isUseCampaignStart()));
      strategyForm.param("use_campaign_end", Utility.getOnOrOff(entity.isUseCampaignEnd()));
      strategyForm.param("status", Utility.getOnOrOff(entity.isStatus()));

      if (entity.getSupplyType() != null) {
        strategyForm.param("supply_type", String.valueOf(entity.getSupplyType()));
      }

      strategyForm.param("use_mm_freq", Utility.getOnOrOff(false));
      if (entity.getGoalType() != null && !entity.getGoalType().equals(goalType.spend)) {
        strategyForm.param("use_optimization", Utility.getOnOrOff(entity.isUseOptimization()));
      }
      if (entity.getVersion() >= 0) {
        strategyForm.param("version", String.valueOf(entity.getVersion()));
      }

      if (entity.getZoneName() != null) {
        strategyForm.param("zone_name", entity.getZoneName());
      }

      if (entity.getType() != null) {
        strategyForm.param("type", entity.getType().toString());
      }
    }
    // strategy domain restrictions
    if (!entity.getStrategyDomainRestrictions().isEmpty()) {
      int inc = 1;
      for (StrategyDomain sd : entity.getStrategyDomainRestrictions()) {
        if (sd != null) {
          strategyForm.param("domains." + inc + ".domain", sd.getDomain());
          strategyForm.param("domains." + inc + ".restriction", sd.getRestriction().name());
          inc++;
        }
      }

    }
    // strategy audio segments
    if (!entity.getAudienceSegments().isEmpty() && entity.getAudienceSegmentExcludeOp() != null
        && entity.getAudienceSegmentIncludeOp() != null) {
      int inc = 1;
      for (Segments sd : entity.getAudienceSegments()) {
        if (sd != null) {
          strategyForm.param("segments." + inc + ".id", String.valueOf(sd.getId()));
          strategyForm.param("segments." + inc + ".restriction", sd.getRestriction().name());

          inc++;
        }
      }
    }
    // target values
    if (!entity.getTargetValues().isEmpty()) {
      int inc = 1;
      String valueIds = "";
      for (TargetValues tv : entity.getTargetValues()) {
        if (tv != null) {
          strategyForm.param("dimensions." + inc + ".code", tv.getCode().name());
          strategyForm.param("dimensions." + inc + ".restriction", tv.getRestriction().name());

          if (!tv.getValueIds().isEmpty()) {
            for (Integer vi : tv.getValueIds()) {
              valueIds += vi.toString();
            }
          }
          strategyForm.param("dimensions." + inc + ".value_ids", valueIds);

          inc++;
        }
      }
    }

    Form finalStrategyForm = Utility.getFilteredForm(strategyForm, "strategy");

    return finalStrategyForm;

  }
}
