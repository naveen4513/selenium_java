@ECHO OFF

echo # IE, CHROME, MOZILLA, PHANTOM, GECKO
if %Browser_Type%==IE (
echo browserType=IE
)

if %Browser_Type%==CHROME (
echo browserType=CHROME
)

if %Browser_Type%==MOZILLA (
echo browserType=MOZILLA
)

if %Browser_Type%==PHANTOM (
echo browserType=PHANTOM
)

if %Browser_Type%==GECKO (
echo browserType=GECKO
)
echo.

echo # SSO, Sirion
if %Client_Login_Type%==SSO (
echo clientLoginType=SSO
)

if %Client_Login_Type%==Sirion (
echo clientLoginType=Sirion
)
echo.

echo #Yes, No
if %Entity_Data_Creation%==true (
echo entityDataCreation=Yes
)

if %Entity_Data_Creation%==false (
echo entityDataCreation=No
)
echo.

echo # End User Details
echo endUserURL=%End_User_URL%
echo endUserUsername=%End_User_UserName%
echo endUserPassword=admin123
echo endUserFullName=%End_User_FullName%
echo.

echo # Client Admin Details
echo clientAdminURL=%Client_Admin_URL%
echo clientAdminUsername=%Client_Admin_UserName%
echo clientAdminPassword=admin123
echo.

echo # Sirion Super Admin Details
echo sirionAdminURL=%Sirion_Admin_URL%
echo sirionAdminUsername=sirion_admin_2
echo sirionAdminPassword=admin123
echo.

echo # Sirion Client Setup Admin
echo ClientSetupAdminURL=%Sirion_Admin_URL%
echo ClientSetupAdminUserName=sirion_client_setup_2
echo ClientSetupAdminPassword=admin123
echo.

echo # Sirion User Admin
echo sirionuserAdminURL=%Sirion_Admin_URL%
echo sirionuserAdminUsername=user_admin_2
echo sirionuserAdminPassword=admin123
echo.

echo # Database Properties
echo DatabaseURL=%Database_Details%
echo DatabaseUsername=postgres
echo DatabasePassword=postgres
echo.

echo # Password Query File Path
echo Databasequeryfilepath=C:\\Users\\TeamAutomation\\.jenkins\\workspace\\SeleniumAutomationProject\\Password Query.sql
echo databaseListingQueryPath=C:\\Users\\TeamAutomation\\.jenkins\\workspace\\SeleniumAutomationProject\\ListingQuery.sql
echo.

echo # TestLink Details
echo testlink_DEV_KEY=1b47ef2a4b42198a288cedaf2abf4d56
echo testlink_SERVER_URL=http://192.168.2.176/testlink/lib/api/xmlrpc/v1/xmlrpc.php
echo testlink_PROJECT_NAME=SirionLabs
echo testlink_PLAN_NAME=Automation Plan
echo testlink_BUILD_NAME=Test Run Smoke
echo.

echo # Listing Page Configuration Details
echo listingColumnCount=10
echo.

echo filterOptionsCount=2
echo filterTCVFrom=1000
echo filterTCVTo=10000
echo filterACVFrom=10000
echo filterACVTo=100000
echo.

echo entityDateFormat=MM-dd-yyyy
echo filterDateFrom=June-20-2017
echo filterDateTo=July-12-2017
echo.

echo maxEntityListingDropdown=100
echo.

echo # Reports Test Case Execution
echo minLargeReportLimit=100
echo maxLargeReportLimit=1000
echo reportDownload=Yes
echo.

echo # Dashboard Test Case Execution
echo dashboardDownload=Yes
echo multipleStakeholders=Yes
echo multipleUsers=Yes
echo stakeholderFilters=2