package com.mediamath.terminalone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.mediamath.terminalone.exceptions.ClientException;
import com.mediamath.terminalone.functional.PostFunctionalTestIT;
import com.mediamath.terminalone.models.JsonResponse;
import com.mediamath.terminalone.models.T1User;
import com.mediamath.terminalone.models.reporting.ReportValidationResponse;
import com.mediamath.terminalone.models.reporting.Reports;
import com.mediamath.terminalone.models.reporting.meta.MetaData;

@RunWith(MockitoJUnitRunner.class)
public class ReportingMockTest {

  private static final String VALIDATE_PERFORMANCE_REPORT = "<?xml version='1.0' ?><result><status code=\"ok\" /></result>";

  private static final String META = "{\"reports\":{\"app_transparency\":{\"Description\":\"Standard performance metrics broken out by App ID. Non-app inventory shown as N/A app_id\",\"Name\":\"App Transparency Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/app_transparency\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/app_transparency/meta\",\"Version\":1},\"audience_index\":{\"Description\":\"Special index metrics for comparing your ads' viewers to 3rd party segments.  Broken out by audience name, as well as standard dimensions down to campaign and strategy.  Currently available in one interval: last 14 days.\",\"Name\":\"Audience Index Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/audience_index\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/audience_index/meta\",\"Version\":1},\"audience_index_pixel\":{\"Description\":\"Special index metrics for comparing your site visitors to 3rd party segments.  Broken out by audience name and pixel.  Currently available in one interval: last 14 days.\",\"Name\":\"Audience Index Pixel Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/audience_index_pixel\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/audience_index_pixel/meta\",\"Version\":1},\"contextual_insights\":{\"Description\":\"Standard performance metrics broken out by contextual categories that strategies are targeting\",\"Name\":\"Contextual Insights\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/contextual_insights\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/contextual_insights/meta\",\"Version\":1},\"data_pixel_loads\":{\"Description\":\"Loads and Uniques metrics for data pixels, broken out by referrer and referrer rank, available by day.\",\"Name\":\"Data Pixel Loads Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/data_pixel_loads\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/data_pixel_loads/meta\",\"Version\":1},\"day_part\":{\"Description\":\"Standard performance metrics broken out by time of day and day of week. Available in standard intervals.\",\"Name\":\"Day Part Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/day_part\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/day_part/meta\",\"Version\":2},\"device_technology\":{\"Description\":\"Standard performance metrics broken out by technology dimensions including browser, operating system, and connection type.  Available in custom date ranges or intervals with the option to aggregate by day, week, or month.\",\"Name\":\"Device Technology Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/device_technology\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/device_technology/meta\",\"Version\":1},\"event_pixel_loads\":{\"Description\":\"Loads and Uniques metrics for event pixels, broken out by referrer and referrer rank, available by day.\",\"Name\":\"Event Pixel Loads Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/event_pixel_loads\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/event_pixel_loads/meta\",\"Version\":1},\"geo\":{\"Description\":\"Standard performance metrics broken out by geographic dimensions including country, region, and metro area.  Available in standard intervals.\",\"Name\":\"Geo Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/geo\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/geo/meta\",\"Version\":2},\"hyperlocal\":{\"Description\":\"Standard performance metrics broken out by Hyperlocal Targeting objects created via third party.\",\"Name\":\"Hyperlocal Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/hyperlocal\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/hyperlocal/meta\",\"Version\":1},\"performance\":{\"Description\":\"Standard performance metrics in campaign currency and broken out by our widest array of dimensions.  Available in custom date ranges or intervals with the option to aggregate by day, week, or month.\",\"Name\":\"Performance Report in Campaign Currency\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/performance\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/performance/meta\",\"Version\":1},\"postal_code\":{\"Description\":\"Standard performance metrics broken out by postal code. Only includes data for strategies that targeted or anti-targeted postal codes. Available in custom date ranges or intervals with the option to aggregate by day, week, or month.\",\"Name\":\"Postal Code Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/postal_code\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/postal_code/meta\",\"Version\":1},\"pulse\":{\"Description\":\"Standard performance metrics broken out by standard dimensions, available in precise time windows - down to the hour - with the option to aggregate by hour or day.\",\"Name\":\"Pulse Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/pulse\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/pulse/meta\",\"Version\":1},\"reach_frequency\":{\"Description\":\"Basic performance metrics as well as the uniques metric, broken out by frequency of ad exposure.  Available in standard intervals.\",\"Name\":\"Reach and Frequency Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/reach_frequency\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/reach_frequency/meta\",\"Version\":1},\"site_transparency\":{\"Description\":\"Standard performance metrics broken out by the domain of the inventory.  Available in standard intervals.\",\"Name\":\"Site Transparency Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/site_transparency\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/site_transparency/meta\",\"Version\":2},\"video\":{\"Description\":\"Video-specific metrics such as completion rate, skips, and fullscreens broken out by a wide array of dimensions.  Available in custom date-ranges or intervals with the option to aggregate by day, week, or month.\",\"Name\":\"Video Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/video\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/video/meta\",\"Version\":1},\"watermark\":{\"Description\":\"Watermark metrics show how many impressions and how much spend went towards the brain's learning activities.  Viewable by campaign and strategy dimensions and available by day.\",\"Name\":\"Watermark Report in US Dollars\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/watermark\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/watermark/meta\",\"Version\":1},\"win_loss\":{\"Description\":\"Metrics describe the auction before a win or even a bid has taken place.  Broken out by strategy, exchange, and deal dimensions and available by hour.\",\"Name\":\"Win/Loss Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/win_loss\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/win_loss/meta\",\"Version\":2},\"win_loss_creative\":{\"Description\":\"Metrics describe the auction before a win has taken place. Broken out by creative dimensions and available by hour.\",\"Name\":\"Win/Loss Creative Report\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/win_loss_creative\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/win_loss_creative/meta\",\"Version\":1}}}";
  
  private static final String REPORTSMETA = "{\"Description\":\"Standard performance metrics broken out by geographic dimensions including country, region, and metro area.  Available in standard intervals.\",\"Name\":\"Geo Report\",\"Type\":\"Standard\",\"URI_Data\":\"https://api.mediamath.com/reporting/v1/std/geo\",\"URI_Meta\":\"https://api.mediamath.com/reporting/v1/std/geo/meta\",\"Version\":2,\"currency\":\"USD\",\"data_retention\":\"last_90_days\",\"default_metrics\":[\"clicks\",\"ctc\",\"ctr\",\"impressions\",\"media_cost\",\"media_cost_cpa\",\"media_cost_cpc\",\"media_cost_cpm\",\"media_cost_pc_cpa\",\"media_cost_pv_cpa\",\"post_click_conversions\",\"post_view_conversions\",\"rr_per_1k_imps\",\"total_ad_cost\",\"total_ad_cost_cpa\",\"total_ad_cost_cpc\",\"total_ad_cost_cpm\",\"total_ad_cost_pc_cpa\",\"total_ad_cost_pv_cpa\",\"total_conversions\",\"total_spend\",\"total_spend_cpa\",\"total_spend_cpc\",\"total_spend_cpm\",\"total_spend_pc_cpa\",\"total_spend_pv_cpa\",\"video_close\",\"video_collapse\",\"video_companion_clicks\",\"video_companion_ctr\",\"video_companion_impressions\",\"video_complete\",\"video_complete_rate\",\"video_engaged_impressions\",\"video_engaged_rate\",\"video_expand\",\"video_first_quartile\",\"video_first_quartile_rate\",\"video_fullscreen\",\"video_midpoint\",\"video_midpoint_rate\",\"video_mute\",\"video_pause\",\"video_play_rate\",\"video_resume\",\"video_rewind\",\"video_skip\",\"video_skippable_impressions\",\"video_skipped_impressions\",\"video_skipped_rate\",\"video_start\",\"video_third_quartile\",\"video_third_quartile_rate\",\"video_unmute\"],\"structure\":{\"dimensions\":{\"advertiser_id\":{\"access\":true,\"name\":\"Advertiser ID\",\"type\":\"id\"},\"advertiser_name\":{\"maxLength\":64,\"name\":\"Advertiser Name\",\"type\":\"string\"},\"agency_id\":{\"access\":true,\"name\":\"Agency ID\",\"type\":\"id\"},\"agency_name\":{\"maxLength\":64,\"name\":\"Agency Name\",\"type\":\"string\"},\"attribution_group\":{\"maxLength\":64,\"name\":\"Attribution Group\",\"type\":\"string\",\"values\":[\"MediaMath Last Touch\",\"Facebook Inc.\"]},\"campaign_budget\":{\"name\":\"Campaign Budget\",\"type\":\"money\"},\"campaign_currency_code\":{\"name\":\"Campaign Currency Code\",\"type\":\"string\"},\"campaign_end_date\":{\"name\":\"Campaign End Date\",\"type\":\"datetime\"},\"campaign_goal_type\":{\"name\":\"Campaign Goal Type\",\"type\":\"string\",\"values\":[\"cpa\",\"cpc\",\"cpe\",\"ctr\",\"reach\",\"roi\",\"spend\",\"vcr\"]},\"campaign_goal_value\":{\"name\":\"Campaign Goal Value\",\"type\":\"money\"},\"campaign_id\":{\"access\":true,\"name\":\"Campaign ID\",\"type\":\"id\"},\"campaign_initial_start_date\":{\"name\":\"Campaign Initial Start Date\",\"type\":\"datetime\"},\"campaign_name\":{\"maxLength\":256,\"name\":\"Campaign Name\",\"type\":\"string\"},\"campaign_start_date\":{\"name\":\"Campaign Start Date\",\"type\":\"datetime\"},\"campaign_timezone\":{\"name\":\"Campaign Time Zone\",\"type\":\"string\"},\"campaign_timezone_code\":{\"name\":\"Campaign Time Zone Code\",\"type\":\"string\"},\"country_code\":{\"maxLength\":50,\"name\":\"Country Code\",\"type\":\"string\"},\"country_name\":{\"maxLength\":200,\"name\":\"Country Name\",\"type\":\"string\"},\"exchange_id\":{\"name\":\"Exchange ID\",\"type\":\"id\"},\"exchange_name\":{\"maxLength\":20,\"name\":\"Exchange Name\",\"type\":\"string\"},\"metro_name\":{\"maxLength\":50,\"name\":\"Metro Name\",\"type\":\"string\"},\"organization_id\":{\"access\":true,\"name\":\"Organization ID\",\"type\":\"id\"},\"organization_name\":{\"maxLength\":64,\"name\":\"Organization Name\",\"type\":\"string\"},\"region_code\":{\"maxLength\":50,\"name\":\"Region Code\",\"type\":\"string\"},\"region_id\":{\"name\":\"Region ID\",\"type\":\"id\"},\"region_name\":{\"maxLength\":200,\"name\":\"Region Name\",\"type\":\"string\"},\"strategy_budget\":{\"name\":\"Strategy Budget\",\"type\":\"money\"},\"strategy_channel\":{\"name\":\"Strategy Channel\",\"type\":\"string\",\"values\":[\"DISPLAY\",\"VIDEO\"]},\"strategy_end_date\":{\"name\":\"Strategy End Date\",\"type\":\"datetime\"},\"strategy_goal_type\":{\"name\":\"Strategy Goal Type\",\"type\":\"string\",\"values\":[\"cpa\",\"cpc\",\"cpe\",\"ctr\",\"reach\",\"roi\",\"spend\",\"vcr\"]},\"strategy_goal_value\":{\"name\":\"Strategy Goal Value\",\"type\":\"money\"},\"strategy_id\":{\"access\":true,\"name\":\"Strategy ID\",\"type\":\"id\"},\"strategy_name\":{\"maxLength\":128,\"name\":\"Strategy Name\",\"type\":\"string\"},\"strategy_start_date\":{\"name\":\"Strategy Start Date\",\"type\":\"datetime\"},\"strategy_supply_type\":{\"maxLength\":64,\"name\":\"Strategy Supply Type\",\"type\":\"string\",\"values\":[\"RTB & Private Market Place (PMP)\",\"External Media Tracking\",\"T1_API\",\"Batch Supply\"]},\"strategy_type\":{\"maxLength\":64,\"name\":\"Strategy Type\",\"type\":\"string\"}},\"metrics\":{\"clicks\":{\"name\":\"Clicks\",\"type\":\"count\"},\"ctc\":{\"name\":\"CTC\",\"type\":\"percent\"},\"ctr\":{\"name\":\"CTR\",\"type\":\"percent\"},\"impressions\":{\"name\":\"Impressions\",\"type\":\"count\"},\"media_cost\":{\"name\":\"Media Cost\",\"type\":\"money\"},\"media_cost_cpa\":{\"name\":\"Media Cost eCPA\",\"type\":\"money\"},\"media_cost_cpc\":{\"name\":\"Media Cost eCPC\",\"type\":\"money\"},\"media_cost_cpm\":{\"name\":\"Media Cost eCPM\",\"type\":\"money\"},\"media_cost_pc_cpa\":{\"name\":\"Media Cost PC CPA\",\"type\":\"money\"},\"media_cost_pv_cpa\":{\"name\":\"Media Cost PV CPA\",\"type\":\"money\"},\"post_click_conversions\":{\"name\":\"Post-Click Conversions\",\"type\":\"count\"},\"post_view_conversions\":{\"name\":\"Post-View Conversions\",\"type\":\"float\"},\"rr_per_1k_imps\":{\"name\":\"Response Rate/1K Imps\",\"type\":\"percent\"},\"total_ad_cost\":{\"name\":\"Total Ad Cost\",\"type\":\"money\"},\"total_ad_cost_cpa\":{\"name\":\"Total Ad Cost eCPA\",\"type\":\"money\"},\"total_ad_cost_cpc\":{\"name\":\"Total Ad Cost eCPC\",\"type\":\"money\"},\"total_ad_cost_cpm\":{\"name\":\"Total Ad Cost eCPM\",\"type\":\"money\"},\"total_ad_cost_pc_cpa\":{\"name\":\"Total Ad Cost PC CPA\",\"type\":\"money\"},\"total_ad_cost_pv_cpa\":{\"name\":\"Total Ad Cost PV CPA\",\"type\":\"money\"},\"total_conversions\":{\"name\":\"Total Conversions\",\"type\":\"float\"},\"total_spend\":{\"name\":\"Total Spend\",\"type\":\"money\"},\"total_spend_cpa\":{\"name\":\"Total Spend eCPA\",\"type\":\"money\"},\"total_spend_cpc\":{\"name\":\"Total Spend eCPC\",\"type\":\"money\"},\"total_spend_cpm\":{\"name\":\"Total Spend eCPM\",\"type\":\"money\"},\"total_spend_pc_cpa\":{\"name\":\"Total Spend PC CPA\",\"type\":\"money\"},\"total_spend_pv_cpa\":{\"name\":\"Total Spend PV CPA\",\"type\":\"money\"},\"video_close\":{\"name\":\"Close\",\"type\":\"count\"},\"video_collapse\":{\"name\":\"Collapse\",\"type\":\"count\"},\"video_companion_clicks\":{\"name\":\"Companion Clicks\",\"type\":\"count\"},\"video_companion_ctr\":{\"name\":\"Companion CTR\",\"type\":\"percent\"},\"video_companion_impressions\":{\"name\":\"Companion Impressions\",\"type\":\"count\"},\"video_complete\":{\"name\":\"100% Completed Views\",\"type\":\"count\"},\"video_complete_rate\":{\"name\":\"100% Completed Rate\",\"type\":\"percent\"},\"video_engaged_impressions\":{\"name\":\"Engaged Impressions\",\"type\":\"count\"},\"video_engaged_rate\":{\"name\":\"Engaged Rate\",\"type\":\"percent\"},\"video_expand\":{\"name\":\"Expand\",\"type\":\"count\"},\"video_first_quartile\":{\"name\":\"25% Completed Views\",\"type\":\"count\"},\"video_first_quartile_rate\":{\"name\":\"25% Completed Rate\",\"type\":\"percent\"},\"video_fullscreen\":{\"name\":\"Fullscreen\",\"type\":\"count\"},\"video_midpoint\":{\"name\":\"50% Completed Views\",\"type\":\"count\"},\"video_midpoint_rate\":{\"name\":\"50% Completed Rate\",\"type\":\"percent\"},\"video_mute\":{\"name\":\"Mute\",\"type\":\"count\"},\"video_pause\":{\"name\":\"Pause\",\"type\":\"count\"},\"video_play_rate\":{\"name\":\"Play Rate\",\"type\":\"percent\"},\"video_resume\":{\"name\":\"Resume\",\"type\":\"count\"},\"video_rewind\":{\"name\":\"Rewind\",\"type\":\"count\"},\"video_skip\":{\"name\":\"Skip\",\"type\":\"count\"},\"video_skippable_impressions\":{\"name\":\"Skippable Impressions\",\"type\":\"count\"},\"video_skipped_impressions\":{\"name\":\"Skipped Impressions\",\"type\":\"count\"},\"video_skipped_rate\":{\"name\":\"Skipped Rate\",\"type\":\"percent\"},\"video_start\":{\"name\":\"Start\",\"type\":\"count\"},\"video_third_quartile\":{\"name\":\"75% Completed Views\",\"type\":\"count\"},\"video_third_quartile_rate\":{\"name\":\"75% Completed Rate\",\"type\":\"percent\"},\"video_unmute\":{\"name\":\"Unmute\",\"type\":\"count\"}},\"time_field\":{\"date\":{\"name\":\"Date\",\"type\":\"datetime\"}}},\"time_aggregation\":\"by_day\",\"time_rollups\":[\"by_day\",\"by_week\",\"by_month\",\"all\"],\"time_windows\":[\"yesterday\",\"last_X_days\",\"month_to_date\",\"campaign_to_date\"],\"timezone\":\"campaign timezone\"}";
  
  private static Properties testConfig = new Properties();
  private static String LOGIN = null;
  
  @Mock
  Connection connectionmock;

  @InjectMocks
  TerminalOne t1 = new TerminalOne();
  
  @Mock 
  Response response;
  
  @Mock
  InputStream stream;
  
  @Mock
  MediaType type;
  
  @BeforeClass
  public static void init() throws Exception {
    InputStream input = PostFunctionalTestIT.class.getClassLoader().getResourceAsStream("mocktest.properties");
    testConfig.load(input);
    LOGIN = testConfig.getProperty("t1.mock.loginResponse");
  }
  
  @After
  public final void tearDown() throws InterruptedException {
    Thread.sleep(5000);
  }

  
  @SuppressWarnings("unchecked")
  @Test
  public void testGetMeta() throws ClientException {

    Mockito.when(connectionmock.post(Mockito.anyString(), Mockito.any(Form.class), Mockito.any(T1User.class))).thenReturn(response);
    Mockito.when(response.readEntity(Mockito.any(Class.class))).thenReturn(LOGIN, META);
    Mockito.when(connectionmock.get(Mockito.anyString(), Mockito.any(T1User.class))).thenReturn(META);

    //login and get the reports.
    t1.authenticate("abc", "xyz", "adfadslfadkfakjf");
    JsonResponse<?> response = t1.getMeta();

    assertNotNull(response.getData());
    
  }
  
  
  @SuppressWarnings("unchecked")
  @Test
  public void testReportsMeta() throws ClientException {
    
    Mockito.when(connectionmock.post(Mockito.anyString(), Mockito.any(Form.class), Mockito.any(T1User.class))).thenReturn(response);
    Mockito.when(response.readEntity(Mockito.any(Class.class))).thenReturn(LOGIN, REPORTSMETA);
    Mockito.when(connectionmock.get(Mockito.anyString(), Mockito.any(T1User.class))).thenReturn(REPORTSMETA);
    
    t1.authenticate("abc", "xyz", "adfadslfadkfakjf");
    MetaData response = t1.getReportsMeta(Reports.GEO);
    
    assertNotNull(response);
    
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testPerformanceReport() throws ClientException, ParseException {
    
    Mockito.when(connectionmock.post(Mockito.anyString(), Mockito.any(Form.class), Mockito.any(T1User.class))).thenReturn(response);
    Mockito.when(connectionmock.getReportData(Mockito.anyString(), Mockito.any(T1User.class))).thenReturn(response);
    Mockito.when(response.getMediaType()).thenReturn(type);
    Mockito.when(response.getMediaType().getType()).thenReturn("text");
    Mockito.when(response.getMediaType().getSubtype()).thenReturn("csv");
    Mockito.when(response.getStatus()).thenReturn(200);
    Mockito.when(response.readEntity(Mockito.any(Class.class))).thenReturn(LOGIN, stream);

    ReportCriteria report = new ReportCriteria();

    report.setDimension("advertiser_name");
    report.setDimension("campaign_id");
    report.setDimension("campaign_name");
    report.setFilter("organization_id", "=", "100048");
    report.setMetric("impressions");
    report.setMetric("clicks");
    report.setMetric("total_conversions");
    report.setMetric("media_cost");
    report.setMetric("total_spend");

    // set having
    // report.setHaving("key1", "=", "val1,val2");

    // set time_rollup
    report.setTime_rollup("by_day");

    // set time_window only when no start date and end date specified.
    // report.setTime_window("last_60_days");

    /*
     * start date & end_date supported format month - YYYY-MM day - YYYY-MM-DD hour - YYYY-MM-DDThh
     * minute - YYYY-MM-DDThh:mi second - YYYY-MM-DDThh:mi:ss
     */
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    String dateInString = "2015-02-06";
    String endDateInString = "2015-04-16";

    String startDate = df.format(df.parse(dateInString));
    String endDate = df.format(df.parse(endDateInString));

    report.setStart_date(startDate);
    report.setEnd_date(endDate);
    
    t1.authenticate("abc", "xyz", "adfadslfadkfakjf");
    BufferedReader reader = t1.getReport(Reports.PERFORMANCE, report);
    
    assertNotNull(reader);
    
  }
  
  
  @SuppressWarnings("unchecked")
  @Test
  public void testValidatePerformanceReport() throws ParseException, ClientException {

    Mockito.when(connectionmock.post(Mockito.anyString(), Mockito.any(Form.class), Mockito.any(T1User.class))).thenReturn(response);
    Mockito.when(connectionmock.getReportData(Mockito.anyString(), Mockito.any(T1User.class))).thenReturn(response);
    
    Mockito.when(response.getMediaType()).thenReturn(type);
    Mockito.when(response.getMediaType().getType()).thenReturn("text");
    Mockito.when(response.getMediaType().getSubtype()).thenReturn("xml");
    Mockito.when(response.readEntity(Mockito.any(Class.class))).thenReturn(LOGIN, VALIDATE_PERFORMANCE_REPORT);
    
    
    ReportCriteria report = new ReportCriteria();

    report.setDimension("advertiser_name");
    report.setDimension("campaign_id");
    report.setDimension("campaign_name");
    report.setFilter("organization_id", "=", "100048");
    report.setMetric("impressions");
    report.setMetric("clicks");
    report.setMetric("total_conversions");
    report.setMetric("media_cost");
    report.setMetric("total_spend");

    // set having
    // report.setHaving("key1", "=", "val1,val2");

    // set time_rollup
    report.setTime_rollup("by_day");

    // set time_window only when no start date and end date specified.
    // report.setTime_window("last_60_days");

    /*
     * start date & end_date supported format month - YYYY-MM day - YYYY-MM-DD hour - YYYY-MM-DDThh
     * minute - YYYY-MM-DDThh:mi second - YYYY-MM-DDThh:mi:ss
     */
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    String dateInString = "2015-02-06";
    String endDateInString = "2015-04-16";

    String startDate = df.format(df.parse(dateInString));
    String endDate = df.format(df.parse(endDateInString));

    report.setStart_date(startDate);
    report.setEnd_date(endDate);
    
    t1.authenticate("abc", "xyz", "adfadslfadkfakjf");
    ReportValidationResponse response = t1.validateReport(Reports.PERFORMANCE, report);
    
    assertNotNull(response);
    assertEquals("ok", response.getStatus()[0].getCode());

  }
  
}
