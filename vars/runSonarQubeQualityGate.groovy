// -----------------------------------------------------------------------------
// Maintained by: Debjyoti Shit
// Description: Run the SonarQube Quality Gate.
// -----------------------------------------------------------------------------
def call() {
    echo "[INFO] Waiting for SonarQube Quality Gate result..."

    timeout(time: 20, unit: 'MINUTES') {
        waitForQualityGate abortPipeline: false
    }

    echo "[INFO] Quality Gate check completed."
}
