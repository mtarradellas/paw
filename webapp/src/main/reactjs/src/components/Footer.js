import React from 'react';

import '../css/footer.css';

function Footer(){
    return <footer>
        <div className={"footer__content"}>
            <div className={"footer__about-us"}>
                <h3>About us</h3>
                <p>Manuel Tarradellas</p>
                <p>Lucía Karpovich</p>
                <p>Facundo Astiz</p>
                <p>Pedro Vedoya</p>
            </div>

            <div className={"footer__find-us"}>
                <h3>About us</h3>
                <p>ITBA, CABA, ARG</p>
                <p>petsociety.contact@gmail.com</p>
            </div>
        </div>
    </footer>;
}

export default Footer;