#ifndef VISUALIZE_H
#define VISUALIZE_H

#include "input.h"
#include <QApplication>

#include "systemsolver.h"

#include <QtDataVisualization/q3dscatter.h>
#include <QtDataVisualization/qabstract3dseries.h>

#include <QtWidgets/QWidget>

#include <QRandomGenerator>

using namespace QtDataVisualization;

QVector3D randVector();
void visualize(std::vector<double>*);

#endif // VISUALIZE_H
