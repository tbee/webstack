if exist javaHome.cmd (
    call javaHome.cmd
)
rem call mvnw jreleaser:config
call mvnw clean deploy jreleaser:full-release -DaltDeploymentRepository=local::file:./target/staging-deploy
pause