import React, { useEffect, useRef } from 'react';

const QRCodeComponent = ({ value, size, fgColor, logo }) => {
    const qrCodeRef = useRef(null);

    useEffect(() => {
        if (typeof window !== 'undefined') {
            import('qr-code-styling').then(({ default: QRCodeStyling }) => {
                // see all options here https://qr-code-styling.com/
                const qrCodeStyling = new QRCodeStyling({
                    width: size,
                    height: size,
                    data: value,
                    image: logo,
                    dotsOptions: {
                        color: fgColor,
                        type: 'classy',                        
                    },
                    // backgroundOptions: {
                    //     color: 'rgba(0, 0, 0, 0)', // Transparent background
                    // },
                    imageOptions: {
                        crossOrigin: 'anonymous',
                        margin: 5,
                        imageSize: 0.25
                    },
                    qrOptions: {
                        typeNumber: 0,
                        mode: 'Byte',
                        errorCorrectionLevel: 'H',
                    },

                    // "cornersSquareOptions": {"type": "extra-rounded", "color": "#000000"},
                    // "cornersSquareOptions": {"type": "extra-rounded"},
                });


                if (qrCodeRef.current) {
                    qrCodeRef.current.innerHTML = ''; // Clear the container
                    qrCodeStyling.append(qrCodeRef.current);
                }
            }).catch(error => console.error('Failed to load QRCodeStyling', error));
        }
    }, [value, size, fgColor, logo]);

    return <div ref={qrCodeRef} style={{ width: size, height: size }} />;
};

export default QRCodeComponent;
