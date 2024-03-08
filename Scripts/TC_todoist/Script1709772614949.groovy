import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.util.internal.PathUtil as PathUtil
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration

Mobile.comment('When user create project from API')

responseCreateProject = WS.sendRequestAndVerify(findTestObject('API/Create Project', [('token') : GlobalVariable.token, ('projectName') : GlobalVariable.projectName]))

projectId = WS.getElementPropertyValue(responseCreateProject, 'id')

'Set global variable project id from response create'
GlobalVariable.projectId = projectId

Mobile.comment('When user login via Mobile')

'Get full directory\'s path of android application'
def appPath = PathUtil.relativeToAbsolutePath(GlobalVariable.G_AppPath, RunConfiguration.getProjectDir())

Mobile.startApplication(appPath, true)

Mobile.tap(findTestObject('Mobile Element/Home - Continue with Email'), 20)

Mobile.tap(findTestObject('Mobile Element/Home - Login with Email'), 10)

Mobile.setText(findTestObject('Mobile Element/Login - Input email'), GlobalVariable.email, 10)

Mobile.tap(findTestObject('Mobile Element/Login - Password'), 10)

Mobile.setText(findTestObject('Mobile Element/Login - Input password'), GlobalVariable.password, 10)

Mobile.tap(findTestObject('Mobile Element/Login - Log in button'), 10)

Mobile.comment('And user goes to Project List')

Mobile.tap(findTestObject('Mobile Element/Navigation - Browse'), 20)

Mobile.tap(findTestObject('Mobile Element/My Projects - Manage projects button'), 10)

Mobile.comment('Then project that created via API is displayed in Mobile')

Mobile.setText(findTestObject('Mobile Element/Project List - Search project'), 'API', 10)

Mobile.tap(findTestObject('Mobile Element/Project List - First Result'), 10)

'Get displayed name in first row from project list'
def text = Mobile.getText(findTestObject('Mobile Element/Project - Name'), 10)

Mobile.verifyEqual(text, 'Test from API')

Mobile.comment('Given user create task from Mobile')

Mobile.tap(findTestObject('Mobile Element/Task - Add button'), 10)

Mobile.setText(findTestObject('Mobile Element/New Task - Name'), GlobalVariable.taskName, 10)

Mobile.tap(findTestObject('Mobile Element/New Task - Description'), 10)

Mobile.setText(findTestObject('Mobile Element/New Task - Description'), GlobalVariable.taskDescription, 10)

Mobile.tap(findTestObject('Mobile Element/New Task - Due date'), 10)

Mobile.tap(findTestObject('Mobile Element/New Task - Due date Tomorrow'), 10)

Mobile.tap(findTestObject('Mobile Element/New Task - Save'), 10)

Mobile.hideKeyboard()

Mobile.verifyElementVisible(findTestObject('Mobile Element/Task List - Name'), 0)

Mobile.closeApplication()

Mobile.comment('Then task that created via Mobile is displayed in API')

WS.sendRequestAndVerify(findTestObject('API/Get Task by project id', [('projectId') : GlobalVariable.projectId]))

'Delete Project for clean the data'
WS.sendRequestAndVerify(findTestObject('API/Delete Project by id', [('projectId') : GlobalVariable.projectId]))

