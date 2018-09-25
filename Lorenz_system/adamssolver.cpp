#include "adamssolver.h"
#include "rungekuttasolver.h"

AdamsSolver::AdamsSolver(InputParams input, std::string name)
    : SystemSolver (input, name)
{

}

/**
 * @see http://www.universityofcalicut.info/SDE/BSc_maths_numerical_methods.pdf
 */
std::vector<double> *AdamsSolver::solve(double time) {
    RungeKuttaSolver starterValues(input, "Explicit Euler");
    std::vector<double> *triple = starterValues.solve(dt * 4 + 1); // Hacked to get exactly 4 first values

    std::vector<double>& xk = triple[0];
    std::vector<double>& yk = triple[1];
    std::vector<double>& zk = triple[2];

    xk.resize(4);
    yk.resize(4);
    zk.resize(4);

    double currentTime = dt * 4;
    unsigned int n = 3;

    while (currentTime < time - dt) {

        double x_pred = xk[n] + (dt / 24) * (55 * x(xk[n], yk[n], zk[n]) - 59 * x(xk[n-1], yk[n-1], zk[n-1]) + 37 * x(xk[n-2], yk[n-2], zk[n-2]) - 9 * x(xk[n-3], yk[n-3], zk[n-3]));
        double y_pred = yk[n] + (dt / 24) * (55 * y(xk[n], yk[n], zk[n]) - 59 * y(xk[n-1], yk[n-1], zk[n-1]) + 37 * y(xk[n-2], yk[n-2], zk[n-2]) - 9 * y(xk[n-3], yk[n-3], zk[n-3]));
        double z_pred = zk[n] + (dt / 24) * (55 * z(xk[n], yk[n], zk[n]) - 59 * z(xk[n-1], yk[n-1], zk[n-1]) + 37 * z(xk[n-2], yk[n-2], zk[n-2]) - 9 * z(xk[n-3], yk[n-3], zk[n-3]));

        double x_corr = xk[n] + (dt / 24) * (9 * x(x_pred, y_pred, z_pred) + 19 * x(xk[n], yk[n], zk[n]) - 5 * x(xk[n-1], yk[n-1], zk[n-1]) + x(xk[n-2], yk[n-2], zk[n-2]));
        double y_corr = yk[n] + (dt / 24) * (9 * y(x_pred, y_pred, z_pred) + 19 * y(xk[n], yk[n], zk[n]) - 5 * y(xk[n-1], yk[n-1], zk[n-1]) + y(xk[n-2], yk[n-2], zk[n-2]));
        double z_corr = zk[n] + (dt / 24) * (9 * z(x_pred, y_pred, z_pred) + 19 * z(xk[n], yk[n], zk[n]) - 5 * z(xk[n-1], yk[n-1], zk[n-1]) + z(xk[n-2], yk[n-2], zk[n-2]));

        triple[0].push_back(x_corr);
        triple[1].push_back(y_corr);
        triple[2].push_back(z_corr);
        currentTime += dt;
        n += 1;
    }


    return triple;
}

AdamsSolver::~AdamsSolver() {}
