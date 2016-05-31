/*
 Copyright (c) 2015 The Polymer Project Authors. All rights reserved.
 This code may only be used under the BSD style license found at http://polymer.github.io/LICENSE.txt
 The complete set of authors may be found at http://polymer.github.io/AUTHORS.txt
 The complete set of contributors may be found at http://polymer.github.io/CONTRIBUTORS.txt
 Code distributed by Google as part of the polymer project is also
 subject to an additional IP rights grant found at http://polymer.github.io/PATENTS.txt
 */

(function (document) {
    'use strict';

    // Grab a reference to our auto-binding template
    // and give it some initial binding values
    // Learn more about auto-binding templates at http://goo.gl/Dx1u2g
    var app = document.querySelector('#app');

    // Sets app default base URL
    app.baseUrl = '/';
    if (window.location.port === '') {  // if production
        // Uncomment app.baseURL below and
        // set app.baseURL to '/your-pathname/' if running from folder in production
        // app.baseUrl = '/polymer-starter-kit/';
    }

    app.displayInstalledToast = function () {
        // Check to make sure caching is actually enabled—it won't be in the dev environment.
        if (!Polymer.dom(document).querySelector('platinum-sw-cache').disabled) {
            Polymer.dom(document).querySelector('#caching-complete').show();
        }
    };

    // Listen for template bound event to know when bindings
    // have resolved and content has been stamped to the page
    app.addEventListener('dom-change', function () {
        console.log('Our app is ready to rock!');
    });

    // See https://github.com/Polymer/polymer/issues/1381
    window.addEventListener('WebComponentsReady', function () {
        // imports are loaded and elements have been registered
    });

    // Main area's paper-scroll-header-panel custom condensing transformation of
    // the appName in the middle-container and the bottom title in the bottom-container.
    // The appName is moved to top and shrunk on condensing. The bottom sub title
    // is shrunk to nothing on condensing.
    window.addEventListener('paper-header-transform', function (e) {
        var appName = Polymer.dom(document).querySelector('#mainToolbar .app-name');
        var middleContainer = Polymer.dom(document).querySelector('#mainToolbar .middle-container');
        var bottomContainer = Polymer.dom(document).querySelector('#mainToolbar .bottom-container');
        var detail = e.detail;
        var heightDiff = detail.height - detail.condensedHeight;
        var yRatio = Math.min(1, detail.y / heightDiff);
        // appName max size when condensed. The smaller the number the smaller the condensed size.
        var maxMiddleScale = 0.50;
        var auxHeight = heightDiff - detail.y;
        var auxScale = heightDiff / (1 - maxMiddleScale);
        var scaleMiddle = Math.max(maxMiddleScale, auxHeight / auxScale + maxMiddleScale);
        var scaleBottom = 1 - yRatio;

        // Move/translate middleContainer
        Polymer.Base.transform('translate3d(0,' + yRatio * 100 + '%,0)', middleContainer);

        // Scale bottomContainer and bottom sub title to nothing and back
        Polymer.Base.transform('scale(' + scaleBottom + ') translateZ(0)', bottomContainer);

        // Scale middleContainer appName
        Polymer.Base.transform('scale(' + scaleMiddle + ') translateZ(0)', appName);
    });

    // Scroll page to top and expand header
    app.scrollPageToTop = function () {
        app.$.headerPanelMain.scrollToTop(true);
    };

    app.closeDrawer = function () {
        app.$.paperDrawerPanel.closeDrawer();
    };


    //-----------
    app.cols = {style1: '1', style2: '2', style3: '3'};

    // Firebase location
    app.location = 'https://brainlight.firebaseio.com';
    app.usersLocation = 'https://brainlight.firebaseio.com/users';
    app.requestsLocation = 'https://brainlight.firebaseio.com/requests';

    app.userURL = function(userID) {
        return app.location + "/users/" + userID;
    };

    app.requestURL = function(requestID) {
        return app.location + "/requests/" + requestID;
    };
    
    
    app.buildChartData = function() {
        this.data = {
            labels: [],
            datasets: [
                {
                    label: "Low Gamma",
                    fillColor: "rgba(0,0,0,0)",
                    strokeColor: "#fcea0f",
                    pointColor: "#fcea0f",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: []
                },
                {
                    label: "Mid Gamma",
                    fillColor: "rgba(0,0,0,0)",
                    strokeColor: "#f39200",
                    pointColor: "#f39200",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: []
                },
                {
                    label: "Low beta",
                    fillColor: "rgba(0,0,0,0)",
                    strokeColor: "#00a09a",
                    pointColor: "#00a09a",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: []
                },
                {
                    label: "High beta",
                    fillColor: "rgba(0,0,0,0)",
                    strokeColor: "#2eac66",
                    pointColor: "#2eac66",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: []
                },
                {
                    label: "Low Alpha",
                    fillColor: "rgba(0,0,0,0)",
                    strokeColor: "#302683",
                    pointColor: "#302683",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: []
                },
                {
                    label: "High Alpha",
                    fillColor: "rgba(0,0,0,0)",
                    strokeColor: "#1d70b7",
                    pointColor: "#1d70b7",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: []
                },
                {
                    label: "Theta",
                    fillColor: "rgba(0,0,0,0)",
                    strokeColor: "#45405e",
                    pointColor: "#45405e",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: []
                },
                {
                    label: "Delta",
                    fillColor: "rgba(0,0,0,0)",
                    strokeColor: "#9d9c9c",
                    pointColor: "#9d9c9c",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(151,187,205,1)",
                    data: []
                }
            ]
        };
        
        return this.data;
    };
    
    
    app.setChartData = function(self, snapshot) {
        if (self.data.labels.length > 10) {
            self.data.labels = self.data.labels.slice(-10);
            if (self.lowGammaChecked) {
                self.data.datasets[0].data = self.data.datasets[0].data.slice(-10);
            } else {
                self.lowGammaTmp = self.lowGammaTmp.slice(-10);
            }

            if (self.midGammaChecked) {
                self.data.datasets[1].data = self.data.datasets[1].data.slice(-10);
            } else {
                self.midGammaTmp = self.midGammaTmp.slice(-10);
            }

            if (self.lowBetaChecked) {
                self.data.datasets[2].data = self.data.datasets[2].data.slice(-10);
            } else {
                self.lowBetaTmp = self.lowBetaTmp.slice(-10);
            }

            if (self.highBetaChecked) {
                self.data.datasets[3].data = self.data.datasets[3].data.slice(-10);
            } else {
                self.highBetaTmp = self.highBetaTmp.slice(-10);
            }

            if (self.lowAlphaChecked) {
                self.data.datasets[4].data = self.data.datasets[4].data.slice(-10);
            } else {
                self.lowAlphaTmp = self.lowAlphaTmp.slice(-10);
            }

            if (self.highAlphaChecked) {
                self.data.datasets[5].data = self.data.datasets[5].data.slice(-10);
            } else {
                self.highAlphaTmp = self.highAlphaTmp.slice(-10);
            }

            if (self.thetaChecked) {
                self.data.datasets[6].data = self.data.datasets[6].data.slice(-10);
            } else {
                self.thetaTmp = self.thetaTmp.slice(-10);
            }

            if (self.deltaChecked) {
                self.data.datasets[7].data = self.data.datasets[7].data.slice(-10);
            } else {
                self.deltaTmp = self.deltaTmp.slice(-10);
            }
        }

        var data = snapshot.val();
        if (self.lowGammaChecked) {
            self.data.datasets[0].data.push(data.low_gamma);
        } else {
            self.lowGammaTmp.push(data.low_gamma);
        }

        if (self.midGammaChecked) {
            self.data.datasets[1].data.push(data.mid_gamma);
        } else {
            self.midGammaTmp.push(data.mid_gamma);
        }

        if (self.lowBetaChecked) {
            self.data.datasets[2].data.push(data.low_beta);
        } else {
            self.lowBetaTmp.push(data.low_beta);
        }

        if (self.highBetaChecked) {
            self.data.datasets[3].data.push(data.high_beta);
        } else {
            self.highBetaTmp.push(data.high_beta);
        }

        if (self.lowAlphaChecked) {
            self.data.datasets[4].data.push(data.low_alpha);
        } else {
            self.lowAlphaTmp.push(data.low_alpha);
        }

        if (self.highAlphaChecked) {
            self.data.datasets[5].data.push(data.high_alpha);
        } else {
            self.highAlphaTmp.push(data.high_alpha);
        }

        if (self.thetaChecked) {
            self.data.datasets[6].data.push(data.theta);
        } else {
            self.thetaTmp.push(data.theta);
        }

        if (self.deltaChecked) {
            self.data.datasets[7].data.push(data.delta);
        } else {
            self.deltaTmp.push(data.delta);
        }

        self.data.labels.push(data.elapsedTime);
        self.$.chart.updateChart();
    };



// Sign out user
    app.signOut = function () {
        this.$.data.signOut();
    };

// Sign in user
    app.signIn = function (e) {
        this.$.data.signIn(e.detail.params);
        var pages = document.getElementById('pages');
        pages.selectNext();
    };


})(document);
