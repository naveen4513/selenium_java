@ECHO OFF
echo ^<?xml version="1.0" encoding="UTF-8"?^>
echo ^<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd"^>
echo ^<suite name="Suite"^>
echo.
echo ^<listeners^>
echo    ^<listener class-name="org.uncommons.reportng.HTMLReporter" /^>
echo    ^<listener class-name="org.uncommons.reportng.JUnitXMLReporter" /^>
echo ^</listeners^>
echo.
echo ^<suite-files^>
echo.

if %Sirion_Admin_Suite%==true (
echo  		^<suite-file path="Sirion Admin Suite.xml" /^>
)

echo.
if %Client_Setup_Admin_Suite%==true (
echo  		^<suite-file path="Client Setup Admin Suite.xml" /^>
)

echo.
if %Sirion_User_Admin_Suite%==true (
echo  		^<suite-file path="Sirion User Admin Suite.xml" /^>
)

echo.
if %Client_Admin_Suite%==true (
echo  		^<suite-file path="Client Admin Suite.xml" /^>
)

echo.
if %Client_User_Admin_Suite%==true (
echo  		^<suite-file path="Client User Admin Suite.xml" /^>
)

echo.
if %Client_Workflow_Configuration_Suite%==true (
echo  		^<suite-file path="Client Workflow Configuration Suite.xml" /^>
)

echo.
if %Client_UI_Content_Setup_Suite%==true (
echo  		^<suite-file path="Client UI Content Setup Suite.xml" /^>
)

echo.
if %Vendor_Hierarchy_Suite%==true (
echo  		^<suite-file path="Vendor Hierarchy Suite.xml" /^>
)

echo.
if %Supplier_Suite%==true (
echo  		^<suite-file path="Supplier Suite.xml" /^>
)

echo.
if %Contract_Suite%==true (
echo  		^<suite-file path="Contract Suite.xml" /^>
)

echo.
if %PO_Suite%==true (
echo  		^<suite-file path="PO Suite.xml" /^>
)

echo.
if %Obligation_Suite%==true (
echo  		^<suite-file path="Obligation Suite.xml" /^>
)

echo.
if %COB_Suite%==true (
echo  		^<suite-file path="childDNO Suite.xml" /^>
)

echo.
if %SL_Suite%==true (
echo  		^<suite-file path="SL Suite.xml" /^>
)

echo.
if %CSL_Suite%==true (
echo  		^<suite-file path="childSL Suite.xml" /^>
)

echo.
if %Action_Suite%==true (
echo  		^<suite-file path="Action Suite.xml" /^>
)

echo.
if %Issue_Suite%==true (
echo  		^<suite-file path="Issue Suite.xml" /^>
)

echo.
if %Dispute_Suite%==true (
echo  		^<suite-file path="Dispute Suite.xml" /^>
)

echo.
if %Invoice_Suite%==true (
echo  		^<suite-file path="Invoice Suite.xml" /^>
)

echo.
if %CR_Suite%==true (
echo  		^<suite-file path="CR Suite.xml" /^>
)

echo.
if %Interpretation_Suite%==true (
echo  		^<suite-file path="Interpretation Suite.xml" /^>
)

echo.
if %WOR_Suite%==true (
echo  		^<suite-file path="WOR Suite.xml" /^>
)

echo.
if %GB_Suite%==true (
echo  		^<suite-file path="GB Suite.xml" /^>
)

echo.
if %Common_Listing_Suite%==true (
echo  		^<suite-file path="Common Listing Suite.xml" /^>
)

echo.
if %Common_Report_Suite%==true (
echo  		^<suite-file path="Common Report Suite.xml" /^>
)

echo.
if %Common_Dashboard_Suite%==true (
echo  		^<suite-file path="Common Dashboard Suite.xml" /^>
)

echo.
echo ^</suite-files^>
echo ^</suite^>
